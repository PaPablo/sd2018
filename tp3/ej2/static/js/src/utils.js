const getChildValue = (parent, childSelector) => {
  try {
    return parent.querySelector(childSelector).value;
  } catch (e) {
    return undefined;
  }
};

export { getChildValue };
