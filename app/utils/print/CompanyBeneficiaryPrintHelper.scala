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

package utils.print

import com.google.inject.Inject
import controllers.register.beneficiaries.companyoremploymentrelated.company.routes._
import models.UserAnswers
import pages.register.beneficiaries.companyoremploymentrelated.company._
import play.api.i18n.Messages
import utils.countryOptions.CountryOptions
import viewmodels.AnswerSection

class CompanyBeneficiaryPrintHelper @Inject()(answerRowConverter: AnswerRowConverter,
                                              countryOptions: CountryOptions
                                 ) {

  def apply(userAnswers: UserAnswers, name: String, index: Int, draftId: String)(implicit messages: Messages) = {

    val bound: answerRowConverter.Bound = answerRowConverter.bind(userAnswers, name, countryOptions)

    val answers = Seq(
        bound.stringQuestion(NamePage(index), "companyBeneficiary.name", NameController.onPageLoad(index, draftId).url),
        bound.yesNoQuestion(IncomeYesNoPage(index), "companyBeneficiary.discretionYesNo", DiscretionYesNoController.onPageLoad(index, draftId).url),
        bound.percentageQuestion(IncomePage(index), "companyBeneficiary.shareOfIncome", ShareOfIncomeController.onPageLoad(index, draftId).url),
        bound.yesNoQuestion(AddressYesNoPage(index), "companyBeneficiary.addressYesNo", AddressYesNoController.onPageLoad(index, draftId).url),
        bound.yesNoQuestion(AddressUKYesNoPage(index), "companyBeneficiary.addressUkYesNo", AddressUkYesNoController.onPageLoad(index, draftId).url),
        bound.addressQuestion(AddressUKPage(index), "companyBeneficiary.ukAddress", UkAddressController.onPageLoad(index, draftId).url),
        bound.addressQuestion(AddressInternationalPage(index), "companyBeneficiary.nonUkAddress", NonUkAddressController.onPageLoad(index, draftId).url)
      ).flatten

    AnswerSection(
      None,
      answers
    )
  }
}
