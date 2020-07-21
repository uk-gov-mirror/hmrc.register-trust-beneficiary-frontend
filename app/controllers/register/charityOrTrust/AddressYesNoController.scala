/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.register.charityOrTrust

import controllers.actions.register._
import controllers.actions._
import forms.YesNoFormProvider
import javax.inject.Inject
import models._
import navigation.Navigator
import pages.register.beneficiaries.charityOrTrust._
import play.api.data.Form
import play.api.i18n._
import play.api.mvc._
import repositories.RegistrationsRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.register.beneficiaries.charityOrTrust.AddressYesNoView

import scala.concurrent.{ExecutionContext, Future}

class AddressYesNoController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            registrationsRepository: RegistrationsRepository,
                                            navigator: Navigator,
                                            identify: RegistrationIdentifierAction,
                                            getData: DraftIdRetrievalActionProvider,
                                            requireData: RegistrationDataRequiredAction,
                                            requiredAnswer: RequiredAnswerActionProvider,
                                            formProvider: YesNoFormProvider,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: AddressYesNoView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form: Form[Boolean] = formProvider.withPrefix("addressYesNo")

  private def actions(draftId: String, index: Int) =
    identify andThen
      getData(draftId) andThen
      requireData andThen
      requiredAnswer(RequiredAnswer(CharityNamePage(index), routes.CharityNameController.onPageLoad(NormalMode, index, draftId)))

  def onPageLoad(mode: Mode, index: Int, draftId: String): Action[AnyContent] = actions(draftId, index) {
    implicit request =>

      val charityName = request.userAnswers.get(CharityNamePage(index)).get.toString

      val preparedForm = request.userAnswers.get(AddressYesNoPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode, draftId, index, charityName))
  }

  def onSubmit(mode: Mode, index: Int, draftId: String): Action[AnyContent] = actions(draftId, index).async {
    implicit request =>

      val charityName = request.userAnswers.get(CharityNamePage(index)).get.toString

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode, draftId, index, charityName))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AddressYesNoPage(index), value))
            _              <- registrationsRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(AddressYesNoPage(index), mode, draftId)(updatedAnswers))
      )
  }
}
