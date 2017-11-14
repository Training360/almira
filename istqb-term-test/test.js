var state = "start"; // start, run or answer

var lang = "en"; // en or us

var item;

var selected;

var counter = {"correct": 0, "incorrect": 0}

$(document).ready(function() {
  $("#en-lang-button").click(selectEn);
  $("#hu-lang-button").click(selectHu);
  updateUi();
});

function answer() {
  state = "answer";
  selected = $('input[name=answer-radio]:checked', '#answer-form').val();
  if (item.answers[selected].correct) {
    counter.correct = counter.correct + 1;
  }
  else {
    counter.incorrect = counter.incorrect + 1;
  }
  updateUi();
}

function next() {
  state = "run";
  generateQuestion();
  updateUi();
}

function selectEn() {
  lang = "en";
  startTest();
}

function selectHu() {
  lang = "hu";
  startTest();
}

function startTest() {
  state = "run";
  generateQuestion();
  updateUi();
}

function randomTerm() {
  if (lang == "en") {
    i = Math.floor((Math.random() * terms.length));
    term = terms[i];
  }
  else {
    i = Math.floor((Math.random() * huTerms.length));
    term = huTerms[i];
  }
  return term;
}

function generateQuestion() {
  var term = randomTerm();

  typeIndex = Math.floor((Math.random() * 2));
  //typeIndex = 0;

  if (typeIndex == 0) {
    var questionText = "Melyik leírás felel meg az alábbi definíciónak?";
    var questionPartFunct = function(term) {
      return term.name;
    };
    var answerPartFunct = function(term) {
      return term.desc;
    };
  }
  else {
    var questionText = "Melyik definíció felel meg az alábbi leírásnak?";
    var questionPartFunct = function(term) {
      return term.desc;
    };
    var answerPartFunct = function(term) {
      return term.name;
    };
  }

    item = {
      question: questionText,
      questionPart: questionPartFunct(term),
      answers: [
      ]
    };
    correct = Math.floor((Math.random() * 4));
    for (var i = 0; i < 4; i++) {
      if (i == correct) {
        var correctAnswer = {"answerPart": answerPartFunct(term), "correct": "true"};
        item.answers.push(correctAnswer);
      }
      else {
        anotherTerm = randomTerm();
        var incorrectAnswer =  {"answerPart": answerPartFunct(anotherTerm)};
        item.answers.push(incorrectAnswer);
      }
    }
}

function updateUi() {
  if (state == "start") {
    $("#start-test-div").show();
    $("#test-div").hide();
  }
  else {
    updateItem();
    $("#start-test-div").hide();
    $("#test-div").show();
  }
}

function updateItem() {
    var tmpl = $.templates("#test-div");
    var data = {"item": item, "state": state, "counter": counter};
    if (state == "answer") {
      if (item.answers[selected].correct) {
        data["answerType"] = "correct";
      }
      else {
        data["answerType"] = "incorrect";
      }
    }
    var html = tmpl.render(data);
    $("#test-div").html(html);
    $("#answer-button").click(answer);
    $("#next-button").click(next);
}
