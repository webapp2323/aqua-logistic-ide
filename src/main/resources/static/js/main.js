$(document).ready(function () {
  $.getJSON('/account', function (data) {
    $('#login').text(data.email);
    $('#avatar').attr("src", data.pictureUrl);
  });

  assignButtons();
  loadPages();
  loadData();
});

function calculatePrice() {
  var countL = $("#quantity").val();
  var price = countL * 2;
  var discount = 0;
  if (countL >= 50 && countL < 100) {
    discount = price * 5 / 100;
  } else if (countL >= 100 && countL < 150) {
    discount = price * 10 / 100;
  } else if (countL >= 150 && countL < 200) {
    discount = price * 15 / 100;
  } else if (countL >= 200) {
    discount = price * 20 / 100;
  }
  price = price - Math.round(discount);
  $("#price").val(price);
}
function deleteTasks() {
  $.getJSON('/deleteTasks?', function (data) {
//TODO:deleteTasks
  });
}

function assignButtons() {
  $("#submitButton").click(function (e) {
    if ($("#address").val() == "") {
      $("#addressSpan").text("Enter address");
    } else {
      $("#addressSpan").text("");
    }

    if ($("#deliveryDate").val() == "") {
      $("#dateSpan").text("Enter date");
    } else {
      $("#dateSpan").text("");
    }

    if ($("#quantity").val() == "") {
      $("#quantitySpan").text("Enter quantity");
    } else {
      $("#quantitySpan").text("");
    }

    if ($("#phone").val() == "") {
      $("#phoneSpan").text("Enter phone");
    } else {
      $("#phoneSpan").text("");
    }

    if ($("#deliveryDate").val() != "" && $("#address").val() != ""
            && $("#phone").val() != "" && $("#quantity").val() != "") {
      const task = {
        date: $("#deliveryDate").val(),
        address: $("#address").val(),
        quantity: $("#quantity").val(),
        price: $("#price").val(),
        phone: $("#phone").val()
      };

      $.ajax({
        type: "POST",
        url: "/addTask",
        contentType: "application/json",
        data: JSON.stringify(task),
        success: function () {
          $("#messageSpan").text("Added successfully");
          loadData();
        },
        error: function (xhr, status, error) {
          $("#messageSpan").text("Error occurred!");
          $("#messageSpan").text("Result: " + status + " " + error + " " +
              xhr.status + " ");
        },
      });
    }
  });

  $('#deleteButton').click(function () {
    var idList = {'toDelete': []};

    $(":checked").each(function () {
      idList['toDelete'].push($(this).val());
    });

    $.post("/delete", idList, function (data, status) {
      window.location = "/";
    });
  });
}

function loadPages() {
  $("#pages").empty();

  $.getJSON('/count', function (data) {
    var pageCount = (data.count / data.pageSize) +
        (data.count % data.pageSize > 0 ? 1 : 0);
    var i;

    for (i = 1; i <= pageCount; i++) {
      $('#pages').append(
          $('<li>').attr('class', 'page-item').append(
              $('<a>').attr('class', 'page-link').attr('id', i - 1)
              .append('Page ' + i))
      );
    }
  });

  $("#pages").on("click", ".page-link", function (event) {
    loadData();
  });
}

function loadData() {
  $("#data > tbody").empty();

  $.getJSON('/tasks?', function (data) {
    var i;

    for (i = 0; i < data.length; i++) {
      $('#data > tbody:last-child').append(
          $('<tr>')
          .append(
              $('<td>').append(
                  $('<input>').attr('type', 'checkbox').attr('value',
                      data[i].id)
              )
          )
          .append($('<td>').append(data[i].date.replace('T', '  ')))
          .append($('<td>').append(data[i].address))
          .append($('<td>').append(data[i].phone))
          .append($('<td>').append(data[i].quantity))
          .append($('<td>').append(data[i].price))
      );
    }
  });
}
