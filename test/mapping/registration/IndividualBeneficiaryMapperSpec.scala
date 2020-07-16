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

import java.time.LocalDate

import base.SpecBase
import generators.Generators
import mapping._
import models.core.pages.{FullName, UKAddress}
import models.registration.pages.PassportOrIdCardDetails
import models.registration.pages.RoleInCompany.Director
import org.scalatest.{MustMatchers, OptionValues}
import pages.register.beneficiaries.individual._

class IndividualBeneficiaryMapperSpec extends SpecBase with MustMatchers
  with OptionValues with Generators {

  private val individualBeneficiariesMapper: Mapping[List[IndividualDetailsType]] = injector.instanceOf[IndividualBeneficiaryMapper]
  private val index0 = 0
  private val index1 = 1
  private val dateOfBirth = LocalDate.of(2010, 10, 10)

  "IndividualBeneficiariesMapper" when {

    "when user answers is empty" must {

      "must not be able to create IndividualDetailsType" in {

        val userAnswers = emptyUserAnswers

        individualBeneficiariesMapper.build(userAnswers) mustNot be(defined)
      }
    }

    "when user answers is not empty" must {

      "must be able to create IndividualDetailsType" when {
        "Nino is set" in {

          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), true).success.value
              .set(DateOfBirthPage(index0), dateOfBirth).success.value
              .set(IncomeYesNoPage(index0), false).success.value
              .set(IncomePage(index0), "100").success.value
              .set(NationalInsuranceYesNoPage(index0), true).success.value
              .set(NationalInsuranceNumberPage(index0), "AB123456C").success.value
              .set(VulnerableYesNoPage(index0), true).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = Some(dateOfBirth),
            vulnerableBeneficiary = true,
            beneficiaryType = None,
            beneficiaryDiscretion = false,
            beneficiaryShareOfIncome = Some("100"),
            identification = Some(IdentificationType(nino = Some("AB123456C"), None, None))
          )
        }

        "Role In Company is set" in {
          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), true).success.value
              .set(DateOfBirthPage(index0), dateOfBirth).success.value
              .set(IncomeYesNoPage(index0), false).success.value
              .set(IncomePage(index0), "100").success.value
              .set(RoleInCompanyPage(index0), Director).success.value
              .set(VulnerableYesNoPage(index0), true).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = Some(dateOfBirth),
            vulnerableBeneficiary = true,
            beneficiaryType = Some(Director.toString),
            beneficiaryDiscretion = false,
            beneficiaryShareOfIncome = Some("100"),
            identification = None
          )
        }

        "UK Address is set" in {
          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), true).success.value
              .set(DateOfBirthPage(index0), dateOfBirth).success.value
              .set(IncomeYesNoPage(index0), true).success.value
              .set(NationalInsuranceYesNoPage(index0), false).success.value
              .set(VulnerableYesNoPage(index0), false).success.value
              .set(AddressYesNoPage(index0), true).success.value
              .set(AddressUKYesNoPage(index0), true).success.value
              .set(AddressUKPage(index0),
                UKAddress("Line1", "Line2", None, Some("Newcastle"), "NE62RT")).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = Some(dateOfBirth),
            vulnerableBeneficiary = false,
            beneficiaryType = None,
            beneficiaryDiscretion = true,
            beneficiaryShareOfIncome = None,
            identification = Some(IdentificationType(
              nino = None,
              None,
              address = Some(
                AddressType("Line1", "Line2", None, Some("Newcastle"), Some("NE62RT"), "GB")
              )
            ))
          )
        }

        "Nino And Address are not set" in {
          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), false).success.value
              .set(IncomeYesNoPage(index0), true).success.value
              .set(NationalInsuranceYesNoPage(index0), false).success.value
              .set(AddressYesNoPage(index0), false).success.value
              .set(VulnerableYesNoPage(index0), false).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = None,
            vulnerableBeneficiary = false,
            beneficiaryType = None,
            beneficiaryDiscretion = true,
            beneficiaryShareOfIncome = None,
            identification = None
          )
        }

        "Passport is set" in {
          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), false).success.value
              .set(IncomeYesNoPage(index0), true).success.value
              .set(NationalInsuranceYesNoPage(index0), false).success.value
              .set(AddressYesNoPage(index0), false).success.value
              .set(VulnerableYesNoPage(index0), false).success.value
              .set(PassportDetailsYesNoPage(index0), true).success.value
              .set(PassportDetailsPage(index0), PassportOrIdCardDetails("GB", "012345678", LocalDate.of(2024, 5, 13))).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = None,
            vulnerableBeneficiary = false,
            beneficiaryType = None,
            beneficiaryDiscretion = true,
            beneficiaryShareOfIncome = None,
            identification = Some(IdentificationType(
              None,
              Some(PassportType("012345678", LocalDate.of(2024, 5, 13), "GB")),
              None
            ))
          )
        }
        "Id card is set" in {
          val userAnswers =
            emptyUserAnswers
              .set(NamePage(index0), FullName("first name", None, "last name")).success.value
              .set(DateOfBirthYesNoPage(index0), false).success.value
              .set(IncomeYesNoPage(index0), true).success.value
              .set(NationalInsuranceYesNoPage(index0), false).success.value
              .set(AddressYesNoPage(index0), false).success.value
              .set(VulnerableYesNoPage(index0), false).success.value
              .set(PassportDetailsYesNoPage(index0), false).success.value
              .set(IdCardDetailsYesNoPage(index0), true).success.value
              .set(IdCardDetailsPage(index0), PassportOrIdCardDetails("GB", "012345678", LocalDate.of(2024, 5, 13))).success.value

          val individuals = individualBeneficiariesMapper.build(userAnswers)

          individuals mustBe defined
          individuals.value.head mustBe IndividualDetailsType(
            name = NameType("first name", None, "last name"),
            dateOfBirth = None,
            vulnerableBeneficiary = false,
            beneficiaryType = None,
            beneficiaryDiscretion = true,
            beneficiaryShareOfIncome = None,
            identification = Some(IdentificationType(
              None,
              Some(PassportType("012345678", LocalDate.of(2024, 5, 13), "GB")),
              None
            ))
          )
        }
      }

      "must be able to create multiple IndividualDetailsType, first with Nino and second with UKAddress" in {
        val userAnswers =
          emptyUserAnswers
            .set(NamePage(index0), FullName("first name", None, "last name")).success.value
            .set(DateOfBirthYesNoPage(index0), true).success.value
            .set(DateOfBirthPage(index0), dateOfBirth).success.value
            .set(IncomeYesNoPage(index0), false).success.value
            .set(IncomePage(index0), "100").success.value
            .set(NationalInsuranceYesNoPage(index0), true).success.value
            .set(NationalInsuranceNumberPage(index0), "AB123456C").success.value
            .set(VulnerableYesNoPage(index0), true).success.value

            .set(NamePage(index1), FullName("first name", None, "last name")).success.value
            .set(DateOfBirthYesNoPage(index1), true).success.value
            .set(DateOfBirthPage(index1), dateOfBirth).success.value
            .set(IncomeYesNoPage(index1), false).success.value
            .set(IncomePage(index1), "100").success.value
            .set(NationalInsuranceYesNoPage(index1), false).success.value
            .set(VulnerableYesNoPage(index1), false).success.value
            .set(AddressYesNoPage(index1), true).success.value
            .set(AddressUKYesNoPage(index1), true).success.value
            .set(AddressUKPage(index1),
              UKAddress("line1", "line2", None, None, "NE62RT")).success.value


        val individuals = individualBeneficiariesMapper.build(userAnswers)

        individuals mustBe defined
        individuals.value mustBe
          List(
            IndividualDetailsType(
              name = NameType("first name", None, "last name"),
              dateOfBirth = Some(dateOfBirth),
              vulnerableBeneficiary = true,
              beneficiaryType = None,
              beneficiaryDiscretion = false,
              beneficiaryShareOfIncome = Some("100"),
              identification = Some(
                IdentificationType(
                  nino = Some("AB123456C"),
                  passport = None,
                  address = None)
              )),

            IndividualDetailsType(
              name = NameType("first name", None, "last name"),
              dateOfBirth = Some(dateOfBirth),
              vulnerableBeneficiary = false,
              beneficiaryType = None,
              beneficiaryDiscretion = false,
              beneficiaryShareOfIncome = Some("100"),
              identification = Some(
                IdentificationType(
                  nino = None,
                  passport = None,
                  address = Some(
                    AddressType("line1", "line2", None, None, Some("NE62RT"), "GB")
                  ))
              ))
          )
      }

      "must not be able to create IndividualDetailsType when incomplete data " in {
        val userAnswers =
          emptyUserAnswers
            .set(NamePage(index0), FullName("first name", None, "last name")).success.value
            .set(DateOfBirthYesNoPage(index0), false).success.value
            .set(IncomeYesNoPage(index0), true).success.value
            .set(NationalInsuranceYesNoPage(index0), false).success.value
            .set(AddressYesNoPage(index0), false).success.value

        individualBeneficiariesMapper.build(userAnswers) mustNot be(defined)
      }
    }
  }
}
