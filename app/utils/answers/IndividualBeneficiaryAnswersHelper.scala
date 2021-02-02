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

package utils.answers

import controllers.register.beneficiaries.individualBeneficiary.{routes => rts}
import controllers.register.beneficiaries.individualBeneficiary.mld5.{routes => ntRts}
import javax.inject.Inject
import models.UserAnswers
import pages.register.beneficiaries._
import pages.register.beneficiaries.individual._
import pages.register.beneficiaries.individual.mld5._
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import sections.beneficiaries.IndividualBeneficiaries
import utils.answers.CheckAnswersFormatters._
import utils.countryOptions.CountryOptions
import viewmodels.{AnswerRow, AnswerSection}

class IndividualBeneficiaryAnswersHelper @Inject()(countryOptions: CountryOptions)
                                                  (userAnswers: UserAnswers,
                                                   draftId: String,
                                                   canEdit: Boolean)
                                                  (implicit messages: Messages) {

  def individualBeneficiaries: Option[Seq[AnswerSection]] = {
    for {
      beneficiaries <- userAnswers.get(IndividualBeneficiaries)
      indexed = beneficiaries.zipWithIndex
    } yield indexed.map {
      case (_, index) =>

        val questions = individualBeneficiaryRows(index)

        AnswerSection(
          Some(Messages("answerPage.section.individualBeneficiary.subheading", index + 1)),
          questions,
          None
        )
    }
  }

  def individualBeneficiaryRows(index: Int): Seq[AnswerRow] = {
    Seq(
      individualBeneficiaryName(index),
      individualBeneficiaryRoleInCompany(index),
      individualBeneficiaryDateOfBirthYesNo(index),
      individualBeneficiaryDateOfBirth(index),
      individualBeneficiaryIncomeYesNo(index),
      individualBeneficiaryIncome(index),
      countryOfNationalityYesNo(index),
      countryOfNationalityInUkYesNo(index),
      countryOfNationality(index),
      individualBeneficiaryNationalInsuranceYesNo(index),
      individualBeneficiaryNationalInsuranceNumber(index),
      countryOfResidenceYesNo(index),
      countryOfResidenceInUkYesNo(index),
      countryOfResidence(index),
      individualBeneficiaryAddressYesNo(index),
      individualBeneficiaryAddressUKYesNo(index),
      individualBeneficiaryAddressUK(index),
      individualBeneficiaryAddressInternational(index),
      individualBeneficiaryPassportDetailsYesNo(index),
      individualBeneficiaryPassportDetails(index),
      individualBeneficiaryIDCardDetailsYesNo(index),
      individualBeneficiaryIDCardDetails(index),
      legallyIncapableYesNo(index),
      individualBeneficiaryVulnerableYesNo(index)
    ).flatten
  }

  def countryOfNationalityYesNo(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfNationalityYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiary.5mld.countryOfNationalityYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(ntRts.CountryOfNationalityYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def countryOfNationalityInUkYesNo(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfNationalityInTheUkYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiary.5mld.countryOfNationalityInTheUkYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(ntRts.CountryOfNationalityInTheUkYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def countryOfNationality(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfNationalityInTheUkYesNoPage(index)) flatMap {
    case false => userAnswers.get(CountryOfNationalityPage(index)) map {
      x =>
        AnswerRow(
          "individualBeneficiary.5mld.countryONationality.checkYourAnswersLabel",
          HtmlFormat.escape(country(x, countryOptions)),
          Some(ntRts.CountryOfNationalityController.onPageLoad(index, draftId).url),
          canEdit = canEdit
        )
    }
    case _ => None
  }

  def countryOfResidenceYesNo(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfResidenceYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiary.5mld.countryOfResidenceYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(ntRts.CountryOfResidenceYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def countryOfResidenceInUkYesNo(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfResidenceInTheUkYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiary.5mld.countryOfResidenceInTheUkYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(ntRts.CountryOfResidenceInTheUkYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def countryOfResidence(index: Int): Option[AnswerRow] = userAnswers.get(CountryOfResidenceInTheUkYesNoPage(index)) flatMap {
    case false => userAnswers.get(CountryOfResidencePage(index)) map {
      x =>
        AnswerRow(
          "individualBeneficiary.5mld.countryOfResidence.checkYourAnswersLabel",
          HtmlFormat.escape(country(x, countryOptions)),
          Some(ntRts.CountryOfResidenceController.onPageLoad(index, draftId).url),
          canEdit = canEdit
        )
    }
    case _ => None
  }

  def legallyIncapableYesNo(index: Int): Option[AnswerRow] = userAnswers.get(LegallyIncapableYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiary.5mld.legallyIncapableYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(ntRts.LegallyIncapableYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryAddressUKYesNo(index: Int): Option[AnswerRow] = userAnswers.get(AddressUKYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryAddressUKYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.AddressUKYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryVulnerableYesNo(index: Int): Option[AnswerRow] = userAnswers.get(VulnerableYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryVulnerableYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.VulnerableYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryPassportDetailsYesNo(index: Int): Option[AnswerRow] = userAnswers.get(PassportDetailsYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryPassportDetailsYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.PassportDetailsYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryPassportDetails(index: Int): Option[AnswerRow] = userAnswers.get(PassportDetailsPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryPassportDetails.checkYourAnswersLabel",
        passportOrIDCard(x, countryOptions),
        Some(rts.PassportDetailsController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryIDCardDetailsYesNo(index: Int): Option[AnswerRow] = userAnswers.get(IDCardDetailsYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryIDCardDetailsYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.IDCardDetailsYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryIDCardDetails(index: Int): Option[AnswerRow] = userAnswers.get(IDCardDetailsPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryIDCardDetails.checkYourAnswersLabel",
        passportOrIDCard(x, countryOptions),
        Some(rts.IDCardDetailsController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryAddressUK(index: Int): Option[AnswerRow] = userAnswers.get(AddressUKPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryAddressUK.checkYourAnswersLabel",
        ukAddress(x),
        Some(rts.AddressUKController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryAddressInternational(index: Int): Option[AnswerRow] = userAnswers.get(AddressInternationalPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryAddressInternational.checkYourAnswersLabel",
        internationalAddress(x, countryOptions),
        Some(rts.AddressInternationalController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryAddressYesNo(index: Int): Option[AnswerRow] = userAnswers.get(individual.AddressYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryAddressYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.AddressYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryNationalInsuranceNumber(index: Int): Option[AnswerRow] =
    userAnswers.get(NationalInsuranceNumberPage(index)) map {
      x =>
        AnswerRow(
          "individualBeneficiaryNationalInsuranceNumber.checkYourAnswersLabel",
          HtmlFormat.escape(formatNino(x)),
          Some(rts.NationalInsuranceNumberController.onPageLoad(index, draftId).url),
          indBeneficiaryName(index, userAnswers),
          canEdit = canEdit
        )
    }

  def individualBeneficiaryNationalInsuranceYesNo(index: Int): Option[AnswerRow] = userAnswers.get(NationalInsuranceYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryNationalInsuranceYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.NationalInsuranceYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryIncome(index: Int): Option[AnswerRow] = userAnswers.get(IncomePage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryIncome.checkYourAnswersLabel",
        percentage(x.toString),
        Some(rts.IncomeController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryIncomeYesNo(index: Int): Option[AnswerRow] = userAnswers.get(IncomeYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryIncomeYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.IncomeYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryDateOfBirth(index: Int): Option[AnswerRow] = userAnswers.get(DateOfBirthPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryDateOfBirth.checkYourAnswersLabel",
        HtmlFormat.escape(x.format(dateFormatter)),
        Some(rts.DateOfBirthController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryDateOfBirthYesNo(index: Int): Option[AnswerRow] = userAnswers.get(DateOfBirthYesNoPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryDateOfBirthYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(rts.DateOfBirthYesNoController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryName(index: Int): Option[AnswerRow] = userAnswers.get(NamePage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryName.checkYourAnswersLabel",
        HtmlFormat.escape(x.displayFullName),
        Some(rts.NameController.onPageLoad(index, draftId).url),
        canEdit = canEdit
      )
  }

  def individualBeneficiaryRoleInCompany(index: Int): Option[AnswerRow] = userAnswers.get(RoleInCompanyPage(index)) map {
    x =>
      AnswerRow(
        "individualBeneficiaryRoleInCompany.checkYourAnswersLabel",
        HtmlFormat.escape(x.toString),
        Some(rts.RoleInCompanyController.onPageLoad(index, draftId).url),
        indBeneficiaryName(index, userAnswers),
        canEdit = canEdit
      )
  }

}
