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

package mapping.registration

import mapping.reads.TrustBeneficiary
import models.BeneficiaryTrustType
import play.api.libs.json.JsPath
import sections.beneficiaries.TrustBeneficiaries

class TrustBeneficiaryMapper extends Mapper[BeneficiaryTrustType, TrustBeneficiary] {

  override def jsPath: JsPath = TrustBeneficiaries.path

  override def beneficiaryType(beneficiary: TrustBeneficiary): BeneficiaryTrustType = BeneficiaryTrustType(
    organisationName = beneficiary.name,
    beneficiaryDiscretion = beneficiary.discretionYesNo,
    beneficiaryShareOfIncome = beneficiary.shareOfIncome.map(_.toString),
    identification = beneficiary.identification,
    countryOfResidence = beneficiary.countryOfResidence
  )

}
