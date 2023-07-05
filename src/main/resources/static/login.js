      $(document).ready(function() {
            $('#loginForm').submit(function(event) {
                event.preventDefault();

                var ssn = $('#ssn').val();
                var password = $('#password').val();

                $.ajax({
                    url: 'http://localhost:8080/api/v1/auth/login',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify({ ssn: ssn, password: password }),
                    contentType: 'application/json',

                    success: function(response) {
                        localStorage.setItem('token', response.token);
                        localStorage.setItem('ssn', response.ssn);
                        localStorage.setItem('id', response.id);
                        localStorage.setItem('firstName', response.firstName);
                        localStorage.setItem('lastName', response.lastName);


                        alert('Token : '+ localStorage.getItem('token'));
                        alert('Full name : '+ localStorage.getItem('firstName') + ' ' + localStorage.getItem('lastName'));
                        alert('SSN : '+ localStorage.getItem('ssn'));
                        alert('ID : '+ localStorage.getItem('id'));

                        window.location.href = 'dashboard.html';
                    },

                    error: function(xhr, status, error) {
                        $('#error-msg').text('Error: ' + xhr.responseText);
                    }

                });
            });
        });