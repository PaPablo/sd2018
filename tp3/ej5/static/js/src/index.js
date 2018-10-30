import {
  getRefreshInterval, 
  onRefreshMessages, 
  onClickMessageSend
} from "./chat";

import { scrollToBottom } from "./utils";

const setMessageSendHandler = () => {
  document.querySelector("#send-button").addEventListener(
    "click", onClickMessageSend);
};

const focusMessageForm = () => {
  const form = document.querySelector("#message-form");
  const input = form.querySelector("input");
  input.focus()
};

window.onload = () => {
  setInterval(onRefreshMessages, getRefreshInterval());
  setMessageSendHandler();
  scrollToBottom(".messages");
  focusMessageForm();
};
