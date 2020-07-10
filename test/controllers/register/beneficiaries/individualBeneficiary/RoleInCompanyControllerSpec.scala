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

package controllers.register.beneficiaries.individualBeneficiary

import base.SpecBase
import forms.RoleInCompanyFormProvider
import models.NormalMode
import models.core.pages.FullName
import models.registration.pages.RoleInCompany.Director
import org.scalatestplus.mockito.MockitoSugar
import pages.register.beneficiaries.individual.{NamePage, RoleInCompanyPage}
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.register.beneficiaries.individualBeneficiary.RoleInCompanyView

class RoleInCompanyControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new RoleInCompanyFormProvider()
  val form = formProvider()
  val name = FullName("FirstName", None, "LastName")
  val index = 0

  val userAnswers = emptyUserAnswers.set(NamePage(index), name).success.value

  def application = applicationBuilder(userAnswers = Some(userAnswers)).build()

  lazy val roleInCompanyControllerRoute = routes.RoleInCompanyController.onPageLoad(NormalMode, index, draftId).url

  "AddressYesNo Controller" must {

    "return OK and the correct view for a GET" in {

      val request = FakeRequest(GET, roleInCompanyControllerRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[RoleInCompanyView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode, draftId, name, 0)(fakeRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(NamePage(index), name).success.value
        .set(RoleInCompanyPage(index), Director).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, roleInCompanyControllerRoute)

      val view = application.injector.instanceOf[RoleInCompanyView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(Director), NormalMode, draftId, name, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val request =
        FakeRequest(POST, roleInCompanyControllerRoute)
          .withFormUrlEncodedBody(("value", Director.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request =
        FakeRequest(POST, roleInCompanyControllerRoute)
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val view = application.injector.instanceOf[RoleInCompanyView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode, draftId, name, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, roleInCompanyControllerRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, roleInCompanyControllerRoute)
          .withFormUrlEncodedBody(("value", "true"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
