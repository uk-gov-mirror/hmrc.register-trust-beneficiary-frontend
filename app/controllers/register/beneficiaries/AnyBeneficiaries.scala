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

package controllers.register.beneficiaries

import models.{Beneficiaries, UserAnswers}
import play.api.libs.json.JsArray
import sections.beneficiaries._

trait AnyBeneficiaries {

  def beneficiaries(userAnswers: UserAnswers): Beneficiaries = Beneficiaries(
    userAnswers.get(IndividualBeneficiaries).getOrElse(List.empty),
    userAnswers.get(ClassOfBeneficiaries).getOrElse(List.empty),
    userAnswers.get(CharityBeneficiaries).getOrElse(List.empty),
    userAnswers.get(TrustBeneficiaries).getOrElse(List.empty),
    userAnswers.get(CompanyBeneficiaries).getOrElse(List.empty),
    userAnswers.get(LargeBeneficiaries).getOrElse(JsArray()),
    userAnswers.get(OtherBeneficiaries).getOrElse(JsArray())
  )

  def isAnyBeneficiaryAdded(userAnswers: UserAnswers): Boolean = {
    val beneficiaryLists: Beneficiaries = beneficiaries(userAnswers)

    beneficiaryLists.individuals.nonEmpty ||
      beneficiaryLists.unidentified.nonEmpty ||
      beneficiaryLists.charities.nonEmpty ||
      beneficiaryLists.trusts.nonEmpty ||
      beneficiaryLists.companies.nonEmpty ||
      beneficiaryLists.large.value.nonEmpty ||
      beneficiaryLists.other.value.nonEmpty
  }
}