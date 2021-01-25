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

import controllers.register.beneficiaries.individualBeneficiary.routes._
import models.ReadableUserAnswers
import models.registration.pages.KindOfTrust.Employees
import pages.Page
import pages.register.KindOfTrustPage
import pages.register.beneficiaries.AnswersPage
import pages.register.beneficiaries.individual._
import play.api.mvc.Call

class IndividualBeneficiaryNavigator extends Navigator {

  override def nextPage(page: Page, draftId: String, userAnswers: ReadableUserAnswers): Call =
    routes(draftId)(page)(userAnswers)

  private def routes(draftId: String): PartialFunction[Page, ReadableUserAnswers => Call] =
    simpleNavigation(draftId) andThen (c => (_:ReadableUserAnswers) => c) orElse
      conditionalNavigation(draftId) orElse
      trustTypeNavigation(draftId)

  private def simpleNavigation(draftId: String): PartialFunction[Page, Call] = {
    case RoleInCompanyPage(index) => DateOfBirthYesNoController.onPageLoad(index, draftId)
    case DateOfBirthPage(index) => IncomeYesNoController.onPageLoad(index, draftId)
    case IncomePage(index) => NationalInsuranceYesNoController.onPageLoad(index, draftId)
    case NationalInsuranceNumberPage(index) => VulnerableYesNoController.onPageLoad(index, draftId)
    case AddressUKPage(index) => PassportDetailsYesNoController.onPageLoad(index, draftId)
    case AddressInternationalPage(index) => PassportDetailsYesNoController.onPageLoad(index, draftId)
    case PassportDetailsPage(index) => VulnerableYesNoController.onPageLoad(index, draftId)
    case IDCardDetailsPage(index) => VulnerableYesNoController.onPageLoad(index, draftId)
    case VulnerableYesNoPage(index) => AnswersController.onPageLoad(index, draftId)
    case AnswersPage => controllers.register.beneficiaries.routes.AddABeneficiaryController.onPageLoad(draftId)
  }

  private def conditionalNavigation(draftId: String): PartialFunction[Page, ReadableUserAnswers => Call] = {
    case page @ DateOfBirthYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = DateOfBirthController.onPageLoad(index, draftId),
        noCall = IncomeYesNoController.onPageLoad(index, draftId)
      )
    case page @ IncomeYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = NationalInsuranceYesNoController.onPageLoad(index, draftId),
        noCall = IncomeController.onPageLoad(index, draftId)
      )
    case page @ NationalInsuranceYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = NationalInsuranceNumberController.onPageLoad(index, draftId),
        noCall = AddressYesNoController.onPageLoad(index, draftId)
      )
    case page @ AddressYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = AddressUKYesNoController.onPageLoad(index, draftId),
        noCall = VulnerableYesNoController.onPageLoad(index, draftId)
      )
    case page @ AddressUKYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = AddressUKController.onPageLoad(index, draftId),
        noCall = AddressInternationalController.onPageLoad(index, draftId)
      )
    case page @ PassportDetailsYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = PassportDetailsController.onPageLoad(index, draftId),
        noCall = IDCardDetailsYesNoController.onPageLoad(index, draftId)
      )
    case page @ IDCardDetailsYesNoPage(index) => ua =>
      yesNoNav(
        ua = ua,
        fromPage = page,
        yesCall = IDCardDetailsController.onPageLoad(index, draftId),
        noCall = VulnerableYesNoController.onPageLoad(index, draftId)
      )
  }

  private def trustTypeNavigation(draftId: String): PartialFunction[Page, ReadableUserAnswers => Call] = {
    case NamePage(index) => ua => ua.get(KindOfTrustPage) match {
      case Some(Employees) => RoleInCompanyController.onPageLoad(index, draftId)
      case _ => DateOfBirthYesNoController.onPageLoad(index, draftId)
    }
  }

}
