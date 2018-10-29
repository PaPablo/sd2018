const encodeObj = obj => {
  //Sacado de acá: https://stackoverflow.com/a/35416293
  return Object.keys(obj).map(
    k => `${encodeURIComponent(k)}=${encodeURIComponent(obj[k])}`).join('&');
}

export { encodeObj };
