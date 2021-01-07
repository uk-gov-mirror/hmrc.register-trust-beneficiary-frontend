/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers.register.beneficiaries.charityortrust.charity

import config.annotations.CharityBeneficiary
import controllers.actions.StandardActionSets
import controllers.actions.register.company.NameRequiredAction
import forms.IncomePercentageFormProvider

import javax.inject.Inject
import navigation.Navigator
import pages.register.beneficiaries.charityortrust.charity.{CharityNamePage, HowMuchIncomePage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.RegistrationsRepository
import services.FeatureFlagService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.register.beneficiaries.charityortrust.charity.HowMuchIncomeView

import scala.concurrent.{ExecutionContext, Future}

class HowMuchIncomeController @Inject()(
                                         val controllerComponents: MessagesControllerComponents,
                                         repository: RegistrationsRepository,
                                         @CharityBeneficiary navigator: Navigator,
                                         standardActionSets: StandardActionSets,
                                         nameAction: NameRequiredAction,
                                         featureFlagService: FeatureFlagService,
                                         formProvider: IncomePercentageFormProvider,
                                         view: HowMuchIncomeView)
                                       (implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form: Form[Int] = formProvider.withPrefix("charity.shareOfIncome")

  def onPageLoad(index: Int, draftId: String): Action[AnyContent] =
    standardActionSets.identifiedUserWithData(draftId).andThen(nameAction(index)){
    implicit request =>

      val charityName = request.userAnswers.get(CharityNamePage(index)).get

      val preparedForm = request.userAnswers.get(HowMuchIncomePage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, draftId, index, charityName))
  }

  def onSubmit(index: Int, draftId: String): Action[AnyContent] =
    standardActionSets.identifiedUserWithData(draftId).andThen(nameAction(index)).async {
    implicit request =>

      val charityName = request.userAnswers.get(CharityNamePage(index)).get

      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(view(formWithErrors, draftId, index, charityName))),

        value => {
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(HowMuchIncomePage(index), value))
            is5mld         <- featureFlagService.is5mldEnabled()
            _              <- repository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(HowMuchIncomePage(index), draftId, is5mld, updatedAnswers))
        }
      )
  }
}
