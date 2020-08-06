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
import models.UserAnswers
import play.api.i18n.Messages
import sections.beneficiaries.LargeBeneficiaries
import utils.print.EmploymentRelatedBeneficiaryPrintHelper
import viewmodels.AnswerSection

class EmploymentRelatedBeneficiaryAnswersHelper @Inject()(employmentRelatedBeneficiaryPrintHelper: EmploymentRelatedBeneficiaryPrintHelper) {

  def employmentRelatedBeneficiaries(userAnswers: UserAnswers,
                           canEdit: Boolean)(implicit messages: Messages): Option[Seq[AnswerSection]] = {
    for {
      beneficiaries <- userAnswers.get(LargeBeneficiaries)
      indexed = beneficiaries.zipWithIndex
    } yield indexed.map {
      case (beneficiaryViewModel, index) =>
        employmentRelatedBeneficiaryPrintHelper.printSection(userAnswers, beneficiaryViewModel.name.getOrElse(""), index, userAnswers.draftId)
    }
  }
}