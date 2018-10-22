import { sha256 } from "js-sha256";

window.onload = () => {
  console.log("signup.js");
  try {
    const form = document.querySelector("#signup-form");
    setCustomFeedbackMessages(form);
    form.addEventListener("submit", onSignupSubmit);
  } catch (e) {
    /* handle error */
  }
};

const setCustomFeedbackMessages = form => {
  /*
   * Modifica los mensajes por defecto de HTML5 
   * cuando lo ingresado en el <input /> es incorrecto
   */
  const INPUTS = getInputFeedbacks();
  for (const input of form.querySelectorAll("input")) {
    const feedback = INPUTS.find(obj => obj.id === input.id).feedback;
    //Lo siguiente lo saqué de este respuesta de SO:
    //https://stackoverflow.com/questions/5272433/html5-form-required-attribute-set-custom-validation-message?answertab=votes#tab-top
    //En este caso, el que lo llama parece que es el InputElement
    //Por eso, no se puede usar una arrow function (salvo que le bindees 
    //el `input`)
    input.oninvalid = function() { this.setCustomValidity(feedback) };
    input.oninput = function() { this.setCustomValidity("") };
  }
};

const getInputFeedbacks = () => {
  return [
    {id: "nombre", feedback: "Debe ingresar su nombre y apellido"},
    {id: "legajo", feedback: "Debe ingresar un legajo válido"},
    {id: "edad", feedback: "Debe ingresar una edad válida"},
    {id: "password", feedback: "Debe ingresar una contraseña"},
  ];
};

const getChildValue = (parent, childSelector) => {
  try {
   return parent.querySelector(childSelector).value; 
  } catch (e) {
    return undefined;
  }
};

const onSignupSubmit = evt => {
  evt.preventDefault();
  try {
    const form = document.querySelector("#signup-form");

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
    newForm.method = form.method
    newForm.style.display = "none";

    //Hay que agregarlo al `body` para poder mandarlo
    document.body.appendChild(newForm)
    //Lo mandamos (a mano)
    newForm.submit();

    //Devolvemos `false` para que no intente enviar el formulario original
    return false;
  } catch (e) {
    return false;
  }
};

const makeSecureForm = fields => {
  /*
   * Arma un formulario con los campos pasados en el objeto `fields`
   * Si hay un campo `passwords`, lo agrega después de hashearlo con SHA256
   * */
  const form = document.createElement("form");
  for (const _f in fields) {
    const value = fields[_f];
    const field = _f.trim().toLowerCase();
    const input = document.createElement("input");
    input.name = field;
    if (field === "password") {
      input.value = sha256(value);
      form.appendChild(input);
      continue;
    }
    input.value = value;
    form.appendChild(input);
  }

  return form;
}; 
