let chart = new frappe.Chart( "#charts", { // or DOM element
data: {
    labels: ["12am-3am", "3am-6am", "6am-9am", "9am-12pm",
      "12pm-3pm", "3pm-6pm", "6pm-9pm", "9pm-12am"],

    datasets: [
      {
        name: "浏览", chartType: 'line',
        values: [15, 20, -3, -15, 58, 12, -17, 37]
      }
    ],
},

type: 'axis-mixed', // or 'bar', 'line', 'pie', 'percentage'
height: 200,
colors: ['rgb(72, 187, 116)'],

tooltipOptions: {
  formatTooltipX: d => (d + '').toUpperCase(),
  formatTooltipY: d => d,
}
});