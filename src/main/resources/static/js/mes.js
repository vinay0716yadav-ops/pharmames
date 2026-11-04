document.addEventListener("DOMContentLoaded", function () {
  // Real-time GMP Plant Clock
  function updatePlantClock() {
    const clockEl = document.getElementById("plantClock");
    if (clockEl) {
      const now = new Date();
      const timeStr = now.toISOString().replace("T", " ").substring(0, 19) + " UTC";
      clockEl.textContent = timeStr;
    }
  }

  updatePlantClock();
  setInterval(updatePlantClock, 1000);

  // Auto-dismiss alerts after 6 seconds
  const alerts = document.querySelectorAll(".alert");
  alerts.forEach(function (alert) {
    setTimeout(function () {
      alert.style.transition = "opacity 0.5s ease";
      alert.style.opacity = "0";
      setTimeout(function () {
        alert.remove();
      }, 500);
    }, 6000);
  });
});
