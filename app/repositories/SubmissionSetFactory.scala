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

package repositories

import javax.inject.Inject
import mapping.registration.BeneficiariesMapper
import models.Status.Completed
import models.{Status, SubmissionDraftRegistrationPiece, SubmissionDraftSetData, SubmissionDraftStatus, UserAnswers}
import pages.register.RegistrationProgress
import play.api.libs.json.Json

class SubmissionSetFactory @Inject()(registrationProgress: RegistrationProgress, beneficiariesMapper: BeneficiariesMapper) {

  def createFrom(userAnswers: UserAnswers): SubmissionDraftSetData = {
    val status = registrationProgress.beneficiariesStatus(userAnswers)

    SubmissionDraftSetData(
      Json.toJson(userAnswers),
      Some(SubmissionDraftStatus("beneficiaries", status)),
      mappedDataIfCompleted(userAnswers, status)
    )
  }

  private def mappedDataIfCompleted(userAnswers: UserAnswers, status: Option[Status]) = {
    if (status.contains(Completed)) {
      beneficiariesMapper.build(userAnswers) match {
        case Some(assets) => List(SubmissionDraftRegistrationPiece("trust/entities/beneficiary", Json.toJson(assets)))
        case _ => List.empty
      }
    } else {
      List.empty
    }
  }
}
