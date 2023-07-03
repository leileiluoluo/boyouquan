var mybutton = document.getElementById("top-link");
      window.onscroll = function () {
        if (
          document.body.scrollTop > 800 ||
          document.documentElement.scrollTop > 800
        ) {
          mybutton.style.visibility = "visible";
          mybutton.style.opacity = "1";
        } else {
          mybutton.style.visibility = "hidden";
          mybutton.style.opacity = "0";
        }
      };