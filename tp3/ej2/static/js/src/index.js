import $ from "jquery";
import dt from "datatables.net";
import dtConfig from "./dt-config.js";

window.onload = () => {
  dt();
  console.log("/static/js/dist/index.js");
  $("#tabla-alumnos").DataTable(dtConfig);
};
