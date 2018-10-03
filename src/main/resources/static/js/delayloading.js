$(document).ready(function () {
  setTimeout(function () {
    $('.load-delay').each(function () {
      var imagex = $(this);
      var imgOriginal = imagex.data('original');
      $(imagex).attr('src', imgOriginal);
    });
  }, 3000);
});