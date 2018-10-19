window.onload = () => {
  console.log("onload");
}

function onFormSubmit() {
  const username = document.querySelector("#username").value;
  const password = document.querySelector("#password").value;
  const form = document.querySelector("#form");

  const newForm = document.createElement("form");
  newForm.style.display = "none";
  newForm.action = form.action;
  newForm.method = form.method;

  const newUsername = document.createElement("input");
  newUsername.name = "nombre";
  newUsername.value = username;
  newForm.appendChild(newUsername);

  const newPassword = document.createElement("input");
  newPassword.name = "password";
  newPassword.value = sha256(password);
  newForm.appendChild(newPassword);

  document.body.appendChild(newForm);
  newForm.submit();

  return false;
}
