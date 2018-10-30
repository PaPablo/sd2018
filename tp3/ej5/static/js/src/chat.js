import { encodeObj, cleanInput, scrollToBottom } from "./utils";

const REFRESH_INTERVAL = 1000 * 2;

const getRefreshInterval = () => { return REFRESH_INTERVAL; };

const addMessage = ({username, message}) => {
  /* Agrega un mensaje a la ventana de chat */
  const messagesUl = document.querySelector("#messages-list");
  const newMessage = document.createElement("li");
  newMessage.innerHTML = `<b>${username}</b>: ${message.message}`;
  messagesUl.appendChild(newMessage);
};


const getMessage = () => {
  /* Recupera un mensaje del input del chat */
  const form = document.querySelector("#message-form");
  const input = form.querySelector("input");
  return {
    "message": input.value
  };
};

const getMessagesCount = () => {
  /* Devuelve la cantidad de mensajes que hay en el chat */
  const _length = Array.from(
    document.querySelector("#messages-list").children
  ).length

  return _length > 0 ? _length : 0;
};

const onClickMessageSend = (evt) => {
  /* Manejador para el submit del form */
  evt.preventDefault();
  const message = getMessage();
  const encodedMessage = encodeObj(message);

  fetch("", {
    method: "POST",
    body: encodedMessage,
    headers: {
      //Hay que setear esta cabecera para que el servidor 
      //lo reciba como si le estuviésemos mandando un <form>
      "Content-type": "application/x-www-form-urlencoded"
    }
  })
    .then(res => {
      const username = document.querySelector("#username").innerText;
      addMessage({username, message});
      cleanInput("#message-form input");
      scrollToBottom(".messages");
    })
    .catch(err => {
      console.error({err});
    })
};

const onRefreshMessages = () => { 
  /* Manejador para el refresco de los mensajes */
  const msgsCount = getMessagesCount();
  fetch(`?starting=${msgsCount}`)
    .then(res => res.text())
    .then(res => {
      const parser = new DOMParser();
      const html = parser.parseFromString(res, "text/html");
      try {
        const messages = Array.from(html.querySelector("#messages-list").children);
        messages.forEach(msg => {
          document.querySelector("#messages-list").appendChild(msg);
        });
        scrollToBottom(".messages");
      } catch (e) {
        //Como la #messages-list puede estar vacía, 
        //va tirar excepción al querer acceder a ".children"
      }
    })
    .catch(err => {
      console.error({err});
    })
};

export { 
  getRefreshInterval, 
  onClickMessageSend,
  onRefreshMessages 
};
