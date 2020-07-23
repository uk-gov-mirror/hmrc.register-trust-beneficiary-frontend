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

package mapping.reads

import java.time.LocalDate

import models.core.pages.{Address, FullName}
import models.registration.pages.{PassportOrIdCardDetails, RoleInCompany}
import play.api.libs.json.{Format, Json}


final case class IndividualBeneficiary(name: FullName,
                                       roleInCompany: Option[RoleInCompany],
                                       dateOfBirth: Option[LocalDate],
                                       nationalInsuranceNumber: Option[String],
                                       passportDetails: Option[PassportOrIdCardDetails],
                                       idCardDetails: Option[PassportOrIdCardDetails],
                                       address : Option[Address],
                                       vulnerableYesNo: Boolean,
                                       income: Option[Int] ,
                                       incomeYesNo: Boolean
                                      ) {
}

object IndividualBeneficiary {
  implicit val classFormat: Format[IndividualBeneficiary] = Json.format[IndividualBeneficiary]
}
