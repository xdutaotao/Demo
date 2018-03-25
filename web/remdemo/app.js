require('./ index.scss');

let htmlWidth = document.documentElement.clientWidth || document.body.clientWidth;

let htmlDom = document.getElementsByTagName('html')[0];

htmlDom.style.fontSize = htmlDom / 10 + 'px';