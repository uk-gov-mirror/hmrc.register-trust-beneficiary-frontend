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

package navigation

import base.SpecBase
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.register.beneficiaries.trust._
import controllers.register.beneficiaries.charityortrust.trust.routes._

class TrustBeneficiaryNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks  {

  val navigator = new TrustBeneficiaryNavigator

  val index = 0

  "Trust beneficiary navigator" when {

    "Name page -> Discretion yes no page" in {
      navigator.nextPage(NamePage(index), draftId, emptyUserAnswers)
        .mustBe(DiscretionYesNoController.onPageLoad(index, draftId))
    }

    "Discretion yes no page -> Yes -> Address yes no page" in {
      val answers = emptyUserAnswers
        .set(DiscretionYesNoPage(index), true).success.value

      navigator.nextPage(DiscretionYesNoPage(index), draftId, answers)
        .mustBe(AddressYesNoController.onPageLoad(index, draftId))
    }

    "Discretion yes no page -> No -> Share of income page" in {
      val answers = emptyUserAnswers
        .set(DiscretionYesNoPage(index), false).success.value

      navigator.nextPage(DiscretionYesNoPage(index), draftId, answers)
        .mustBe(ShareOfIncomeController.onPageLoad(index, draftId))
    }

    "Share of income page -> Address yes no page" in {
      navigator.nextPage(ShareOfIncomePage(index), draftId, emptyUserAnswers)
        .mustBe(AddressYesNoController.onPageLoad(index, draftId))
    }

    "Address yes no page -> Yes -> Address in the UK yes no page" in {
      val answers = emptyUserAnswers
        .set(AddressYesNoPage(index), true).success.value

      navigator.nextPage(AddressYesNoPage(index), draftId, answers)
        .mustBe(AddressUKYesNoController.onPageLoad(index, draftId))
    }

    "Address in the UK yes no page -> Yes -> UK address page" in {
      val answers = emptyUserAnswers
        .set(AddressUKYesNoPage(index), true).success.value

      navigator.nextPage(AddressUKYesNoPage(index), draftId, answers)
        .mustBe(AddressUKController.onPageLoad(index, draftId))
    }

    "Address in the UK yes no page -> No -> Non-UK address page" in {
      val answers = emptyUserAnswers
        .set(AddressUKYesNoPage(index), false).success.value

      navigator.nextPage(AddressUKYesNoPage(index), draftId, answers)
        .mustBe(AddressInternationalController.onPageLoad(index, draftId))
    }

    "Address yes no page -> No -> Check your answers page" in {
      val answers = emptyUserAnswers
        .set(AddressYesNoPage(index), false).success.value

      navigator.nextPage(AddressYesNoPage(index), draftId, answers)
        .mustBe(AnswersController.onPageLoad(index, draftId))
    }

    "UK address page -> Check your answers page" in {
      val answers = emptyUserAnswers

      navigator.nextPage(AddressUKPage(index), draftId, answers)
        .mustBe(AnswersController.onPageLoad(index, draftId))

    }

    "Non-UK address page -> Check your answers page" in {
      val answers = emptyUserAnswers
        .set(AddressYesNoPage(index), false).success.value

      navigator.nextPage(AddressInternationalPage(index), draftId, answers)
        .mustBe(AnswersController.onPageLoad(index, draftId))

    }
  }
}