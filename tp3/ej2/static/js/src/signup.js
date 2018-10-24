import { setCustomFeedbackMessages, makeSecureForm } from "./forms.js";
import { getChildValue } from "./utils.js";

const SIGNUP_FORM_ID = "#signup-form";

window.onload = () => {
  console.log("signup.js");
  try {
    const form = document.querySelector(SIGNUP_FORM_ID);
    setCustomFeedbackMessages(form);
    form.addEventListener("submit", onSignupSubmit);
  } catch (e) {
    /* handle error */
  }
};


const onSignupSubmit = evt => {
  evt.preventDefault();
  try {
    const form = document.querySelector(SIGNUP_FORM_ID);

    const nombre = getChildValue(form, "#nombre");
    const legajo = getChildValue(form, "#legajo");
    const sexo = getChildValue(form, "#sexo");
    const edad = getChildValue(form, "#edad");
    const password = getChildValue(form, "#password");

    const newForm = makeSecureForm({
      nombre,
      legajo,
      sexo,
      edad,
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

