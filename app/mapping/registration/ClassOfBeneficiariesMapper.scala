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

import mapping.reads.ClassOfBeneficiary
import models.UnidentifiedType
import play.api.libs.json.JsPath
import sections.beneficiaries.ClassOfBeneficiaries

class ClassOfBeneficiariesMapper extends Mapper[UnidentifiedType, ClassOfBeneficiary]{

  override def jsPath: JsPath = ClassOfBeneficiaries.path

  override def beneficiaryType(beneficiary: ClassOfBeneficiary): UnidentifiedType = UnidentifiedType(
    description = beneficiary.description,
    beneficiaryDiscretion = None,
    beneficiaryShareOfIncome = None
  )

}
