import { makeSecureForm } from "../forms.js";

test("should return a form", () => {
  const fields = {
    username: "unUsername",
    password: "unaPassword"
  };
  const form = makeSecureForm(fields);
  expect(form).toBeInstanceOf(HTMLFormElement);
  const inputNodes = Array.from(form.childNodes);
  const passwordField = inputNodes.find(children => children.name === "password");
  expect(passwordField.value.length).toBe(64)
});
