import { setCustomFeedbackMessages, makeSecureForm } from "./forms.js";
import { getChildValue } from "./utils.js";

const LOGIN_FORM_ID = "#login-form";

window.onload = () => {
  console.log("login.js");
  try {
    const form = document.querySelector(LOGIN_FORM_ID);
    setCustomFeedbackMessages(form);
    form.addEventListener("submit", onLoginSubmit);
  } catch (e) {
    /* handle error */
  }
};


const onLoginSubmit = evt => {
  evt.preventDefault();
  try {
    const form = document.querySelector(LOGIN_FORM_ID);

    const legajo = getChildValue(form, "#legajo");
    const password = getChildValue(form, "#password");

    const newForm = makeSecureForm({
      legajo,
      password
    });

    //El nuevo formulario debe tener los mismos atributos que el original
    newForm.action = form.action;
    newForm.method = form.method;
    newForm.style.display = "none";

    //Hay que agregarlo al `body` para poder mandarlo
    document.body.appendChild(newForm);
    //Lo mandamos (a mano)
    newForm.submit();

    //Devolvemos `false` para que no intente enviar el formulario original
    return false;
  } catch (e) {
    return false;
  }
};

