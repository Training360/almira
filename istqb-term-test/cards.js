var i = 0;

var lang = "en"; // en or hu

var question = "desc"; // term or desc

var show = "desc"; // term or desc

var term;

$(document).ready(function() {
  $("#next-button").click(next);
  $("#card").click(flip);
  $("#en-lang-button").click(selectEn);
  $("#hu-lang-button").click(selectHu);
  $("#desc-question-button").click(selectDesc);
  $("#term-question-button").click(selectTerm);
  next();
});

function selectEn() {
  if (lang != "en") {
    findEnTerm();
    lang = "en";
    updateUi();
  }
}

function selectHu() {
  if (lang != "hu") {
    findHuTerm();
    lang = "hu";
    updateUi();
  }
}

function findHuTerm() {
  var found;
  for (huTerm of huTerms) {
    for (engName of huTerm.engNames) {
      if (engName == term.name) {
        found = huTerm;
      }
    }
  }
  if (found) {
    term = found;
  }
}

function findEnTerm() {
  var found;
  for (enTerm of terms) {
    for (engName of term.engNames) {
      if (engName == enTerm.name) {
        found = enTerm;
      }
    }
  }
  if (found) {
    term = found;
  }
}

function selectDesc() {
  question = "desc";
  show = "desc";

  updateUi();
}

function selectTerm() {
  question = "term";
  show = "term";
  
  updateUi();
}

function next() {
  if (lang == "en") {
    i = Math.floor((Math.random() * terms.length));
    term = terms[i];
  }
  else {
    i = Math.floor((Math.random() * huTerms.length));
    term = huTerms[i];
  }

  show = question;

  updateUi();
}

function updateUi() {
  updateCard();
  updateButtons();
}

function updateButtons() {
  $("#en-lang-button").removeClass();
  $("#hu-lang-button").removeClass();
  $("#desc-question-button").removeClass();
  $("#term-question-button").removeClass();
  if (lang == "hu") {
    $("#hu-lang-button").addClass("btn").addClass("btn-primary");
    $("#en-lang-button").addClass("btn").addClass("btn-default");
  }
  else {
    $("#en-lang-button").addClass("btn").addClass("btn-primary");
    $("#hu-lang-button").addClass("btn").addClass("btn-default");
  }
  if (question == "term") {
    $("#term-question-button").addClass("btn").addClass("btn-primary");
    $("#desc-question-button").addClass("btn").addClass("btn-default");
  }
  else {
    $("#desc-question-button").addClass("btn").addClass("btn-primary");
    $("#term-question-button").addClass("btn").addClass("btn-default");
  }

  if (show == "desc") {
    $("#front").show();
    $("#back").hide();
  }
  else {
    $("#front").hide();
    $("#back").show();
  }

}

function updateCard() {
  var tmpl = $.templates("#card");
  var html = tmpl.render(term);
  $("#card").html(html);
}

function flip() {
  if (show == "desc") {
    show = "term";
  }
  else {
    show = "desc";
  }
  updateUi();
}
