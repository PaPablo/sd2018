import { encodeObj } from "./utils";

const REFRESH_INTERVAL = 1000 * 3;

const getRefreshInterval = () => { return REFRESH_INTERVAL; };

const addMessage = ({username, message}) => {
  const messagesUl = document.querySelector("#messages-list");
  const newMessage = document.createElement("li");
  newMessage.innerHTML = `<b>${username}</b>: ${message.message}`;
  messagesUl.appendChild(newMessage);
};

const cleanInput = () => {
  const form = document.querySelector("#message-form");
  const input = form.querySelector("input");
  input.value = "";
};

const getMessage = () => {
  const form = document.querySelector("#message-form");
  const input = form.querySelector("input");
  return {
    "message": input.value
  };
};

const getMessagesCount = () => {
  const _length = Array.from(
    document.querySelector("#messages-list").children
  ).length

  return _length > 0 ? _length : 0;
};

const onClickMessageSend = (evt) => {
  evt.preventDefault();
  console.log("onClickMessageSend"); 
  const message = getMessage();
  const encodedMessage = encodeObj(message);
  console.log({encodedMessage});

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
      cleanInput();
    })
    .catch(err => {
      console.error({err});
    })
};

const onRefreshMessages = () => { 
  console.log("onRefreshMessages"); 
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
