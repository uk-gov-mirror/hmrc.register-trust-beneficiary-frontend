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

package navigation

import controllers.register.beneficiaries.charityortrust.charity.nonTaxable.{routes => nonTaxRts}
import controllers.register.beneficiaries.charityortrust.charity.{routes => rts}
import models.ReadableUserAnswers
import pages.Page
import pages.register.beneficiaries.charityortrust.charity._
import pages.register.beneficiaries.charityortrust.charity.nonTaxable.{CountryOfResidenceInTheUkYesNoPage, CountryOfResidencePage, CountryOfResidenceYesNoPage}
import play.api.mvc.Call

class CharityBeneficiaryNavigator extends Navigator {

  override def nextPage(page: Page, draftId: String, userAnswers: ReadableUserAnswers): Call =
    nextPage(page, draftId, fiveMldEnabled = false, userAnswers)

  override def nextPage(page: Page, draftId: String, fiveMldEnabled: Boolean, userAnswers: ReadableUserAnswers): Call =
    routes(draftId, fiveMldEnabled)(page)(userAnswers)

  private def simpleNavigation(draftId: String, fiveMldEnabled: Boolean): PartialFunction[Page, Call] = {
    case CharityNamePage(index) => rts.AmountDiscretionYesNoController.onPageLoad(index, draftId)
    case HowMuchIncomePage(index) => fiveMldYesNo(draftId, index, fiveMldEnabled)
    case CharityAddressUKPage(index) => rts.CharityAnswersController.onPageLoad(index, draftId)
    case CharityInternationalAddressPage(index) => rts.CharityAnswersController.onPageLoad(index, draftId)
    case CountryOfResidencePage(index) => rts.AddressYesNoController.onPageLoad(index, draftId)
  }

  private def conditionalNavigation(draftId: String, fiveMldEnabled: Boolean): PartialFunction[Page, ReadableUserAnswers => Call] = {
    case AmountDiscretionYesNoPage(index) => ua =>
      yesNoNav(ua, AmountDiscretionYesNoPage(index), fiveMldYesNo(draftId, index, fiveMldEnabled), rts.HowMuchIncomeController.onPageLoad(index, draftId))
    case AddressYesNoPage(index) => ua =>
      yesNoNav(
        ua,
        AddressYesNoPage(index),
        rts.AddressInTheUkYesNoController.onPageLoad(index, draftId),
        rts.CharityAnswersController.onPageLoad(index, draftId)
      )
    case AddressInTheUkYesNoPage(index) => ua =>
      yesNoNav(
        ua,
        AddressInTheUkYesNoPage(index),
        rts.CharityAddressUKController.onPageLoad(index, draftId),
        rts.CharityInternationalAddressController.onPageLoad(index, draftId)
      )
    case CountryOfResidenceYesNoPage(index) => ua =>
      yesNoNav(
        ua,
        CountryOfResidenceYesNoPage(index),
        nonTaxRts.CountryOfResidenceInTheUkYesNoController.onPageLoad(index, draftId),
        rts.AddressYesNoController.onPageLoad(index, draftId)
      )
    case CountryOfResidenceInTheUkYesNoPage(index) => ua =>
      yesNoNav(
        ua,
        CountryOfResidenceInTheUkYesNoPage(index),
        rts.AddressYesNoController.onPageLoad(index, draftId),
        nonTaxRts.CountryOfResidenceController.onPageLoad(index, draftId)
      )
  }

  private def fiveMldYesNo(draftId: String, index: Int, fiveMldEnabled: Boolean): Call = {
    if (fiveMldEnabled) {
      nonTaxRts.CountryOfResidenceYesNoController.onPageLoad(index, draftId)
    } else {
      rts.AddressYesNoController.onPageLoad(index, draftId)
    }
  }

  private def routes(draftId: String, fiveMldEnabled: Boolean): PartialFunction[Page, ReadableUserAnswers => Call] =
    simpleNavigation(draftId, fiveMldEnabled) andThen (c => (_:ReadableUserAnswers) => c) orElse
      conditionalNavigation(draftId, fiveMldEnabled)
}
