import { getLog } from "../main";

test("should return a log string", () => {
  expect(getLog()).toBe("hola como va");
});
