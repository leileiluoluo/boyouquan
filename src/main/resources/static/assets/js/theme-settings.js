if (localStorage.getItem("pref-theme") === "dark") {
    document.body.classList.add("dark");
} else if (localStorage.getItem("pref-theme") === "light") {
    document.body.classList.remove("dark");
} else if (window.matchMedia("(prefers-color-scheme: dark)").matches) {
    document.body.classList.add("dark");
}