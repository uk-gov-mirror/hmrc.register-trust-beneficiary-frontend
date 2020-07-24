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

package mapping.registration

import javax.inject.Inject
import mapping.Mapping
import mapping.reads.{IndividualBeneficiary, TrustBeneficiary}
import models.UserAnswers
import models.core.pages.Address
import models.registration.pages.PassportOrIdCardDetails

class TrustBeneficiaryMapper @Inject()(addressMapper: AddressMapper) extends Mapping[List[BeneficiaryTrustType]] {

  override def build(userAnswers: UserAnswers): Option[List[BeneficiaryTrustType]] = {

    val trustBeneficiaries : List[mapping.reads.TrustBeneficiary] =
      userAnswers.get(mapping.reads.TrustBeneficiaries).getOrElse(List.empty)

    trustBeneficiaries match {
      case Nil => None
      case list =>
        Some(
          list.map { trustBen =>
            BeneficiaryTrustType(
              organisationName = trustBen.name,
              beneficiaryDiscretion = Some(trustBen.discretionYesNo),
              beneficiaryShareOfIncome = trustBen.shareOfIncome.map(_.toString),
              identification = identificationMapper(trustBen)
            )
          }
        )
    }
  }

  private def identificationMapper(trustBen: TrustBeneficiary): IdentificationOrgType = {
    IdentificationOrgType(
      utr = None,
      address = trustBen.address.flatMap{ address =>
        addressMapper.build(address)
      }
    )
  }

}