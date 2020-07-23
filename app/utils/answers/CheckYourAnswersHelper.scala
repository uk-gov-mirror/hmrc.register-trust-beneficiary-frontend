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

package utils.answers

import javax.inject.Inject
import models.{NormalMode, UserAnswers}
import pages.register.beneficiaries.AddABeneficiaryPage
import pages.register.beneficiaries.classofbeneficiaries.ClassBeneficiaryDescriptionPage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import sections.beneficiaries.ClassOfBeneficiaries
import viewmodels.{AnswerRow, AnswerSection}

class CheckYourAnswersHelper @Inject()()
                                      (userAnswers: UserAnswers,
                                       draftId: String,
                                       canEdit: Boolean)
                                      (implicit messages: Messages) {

  def classOfBeneficiaries(individualBeneficiariesExist: Boolean): Option[Seq[AnswerSection]] = {
    for {
      beneficiaries <- userAnswers.get(ClassOfBeneficiaries)
      indexed = beneficiaries.zipWithIndex
    } yield indexed.map {
      case (_, index) =>

        val questions = Seq(
          classBeneficiaryDescription(index)
        ).flatten

        val sectionKey = if (index == 0 && !individualBeneficiariesExist) {
          Some(Messages("answerPage.section.beneficiaries.heading"))
        } else {
          None
        }

        AnswerSection(Some(Messages("answerPage.section.classOfBeneficiary.subheading") + " " + (index + 1)),
          questions, sectionKey)
    }
  }

  def classBeneficiaryDescription(index: Int): Option[AnswerRow] = userAnswers.get(ClassBeneficiaryDescriptionPage(index)) map {
    x =>
      AnswerRow(
        "classBeneficiaryDescription.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.register.beneficiaries.classofbeneficiaries.routes.ClassBeneficiaryDescriptionController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def addABeneficiary(): Option[AnswerRow] = userAnswers.get(AddABeneficiaryPage) map {
    x =>
      AnswerRow(
        "addABeneficiary.checkYourAnswersLabel",
        HtmlFormat.escape(messages(s"addABeneficiary.$x")),
        Some(controllers.register.beneficiaries.routes.AddABeneficiaryController.onPageLoad(draftId).url),
        canEdit = canEdit
      )
  }

}