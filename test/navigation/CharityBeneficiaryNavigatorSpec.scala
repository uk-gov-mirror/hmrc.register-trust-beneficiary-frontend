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
import controllers.register.beneficiaries.charityortrust.charity.routes._
import models.NormalMode
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.register.beneficiaries.charityortrust.charity._

class CharityBeneficiaryNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks {

  val navigator = new CharityBeneficiaryNavigator

  val index = 0

  "Charity beneficiary navigator" must {

    "Name page -> Do you know date of birth page" in {
      navigator.nextPage(CharityNamePage(index), fakeDraftId, emptyUserAnswers)
        .mustBe(AmountDiscretionYesNoController.onPageLoad(index, fakeDraftId))
    }

    "Do trustees have discretion page" when {

      "Yes" must {
        "-> Do you know address page" in {
          val answers = emptyUserAnswers
            .set(AmountDiscretionYesNoPage(index), true).success.value

          navigator.nextPage(AmountDiscretionYesNoPage(index), fakeDraftId, answers)
            .mustBe(AddressYesNoController.onPageLoad(index, fakeDraftId))
        }
      }

      "No" must {
        "-> How much income page" in {
          val answers = emptyUserAnswers
            .set(AmountDiscretionYesNoPage(index), false).success.value

          navigator.nextPage(AmountDiscretionYesNoPage(index), fakeDraftId, answers)
            .mustBe(HowMuchIncomeController.onPageLoad(index, fakeDraftId))
        }
      }
    }

    "How much income page -> Do you know address page" in {
      navigator.nextPage(HowMuchIncomePage(index), fakeDraftId, emptyUserAnswers)
        .mustBe(AddressYesNoController.onPageLoad(index, fakeDraftId))
    }

    "Do you know address page" when {

      "Yes" must {
        "-> Is address in UK page" in {
          val answers = emptyUserAnswers
            .set(AddressYesNoPage(index), true).success.value

          navigator.nextPage(AddressYesNoPage(index), fakeDraftId, answers)
            .mustBe(AddressInTheUkYesNoController.onPageLoad(index, fakeDraftId))
        }
      }

      "No" must {
        "-> Check answers page" in {
          val answers = emptyUserAnswers
            .set(AddressYesNoPage(index), false).success.value

          navigator.nextPage(AddressYesNoPage(index), fakeDraftId, answers)
            .mustBe(CharityAnswersController.onPageLoad(index, fakeDraftId))
        }
      }
    }

    "Is address in UK page" when {

      "Yes" must {
        "-> UK address page" in {
          val answers = emptyUserAnswers
            .set(AddressInTheUkYesNoPage(index), true).success.value

          navigator.nextPage(AddressInTheUkYesNoPage(index), fakeDraftId, answers)
            .mustBe(CharityAddressUKController.onPageLoad(index, fakeDraftId))
        }
      }

      "No" must {
        "-> International address page" in {
          val answers = emptyUserAnswers
            .set(AddressInTheUkYesNoPage(index), false).success.value

          navigator.nextPage(AddressInTheUkYesNoPage(index), fakeDraftId, answers)
            .mustBe(CharityInternationalAddressController.onPageLoad(index, fakeDraftId))
        }
      }
    }

    "UK address page -> Check answers page" in {
      navigator.nextPage(CharityAddressUKPage(index), fakeDraftId, emptyUserAnswers)
        .mustBe(CharityAnswersController.onPageLoad(index, fakeDraftId))
    }

    "International address page -> Check answers page" in {
      navigator.nextPage(CharityInternationalAddressPage(index), fakeDraftId, emptyUserAnswers)
        .mustBe(CharityAnswersController.onPageLoad(index, fakeDraftId))
    }
  }
}