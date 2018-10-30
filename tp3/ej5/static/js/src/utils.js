const encodeObj = obj => {
  /* Codifica un objeto como si fuese una URL */
  //Sacado de acá: https://stackoverflow.com/a/35416293
  return Object.keys(obj).map(
    k => `${encodeURIComponent(k)}=${encodeURIComponent(obj[k])}`).join('&');
}

const cleanInput = querySelector => {
  /* Limpia el input del chat */
  document.querySelector(querySelector).value = ""
};

const scrollToBottom = querySelector => {
  const el = document.querySelector(querySelector);
  el.scrollTop = el.scrollHeight;
};

export { 
  encodeObj,
  cleanInput,
  scrollToBottom
};
