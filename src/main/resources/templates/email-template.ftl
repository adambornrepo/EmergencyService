<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        .container {
            margin-top: 70px;
            display: block;
            width: 465px;
            height: 600px;
            position: relative;

            background-image: url("cid:backgroundImage");
            background-repeat: no-repeat;
            background-size: cover;
            font-family: Courier, monospace;
            color: #444444;
        }
        .blankA {
                 display: block;
                 width: 465px;
                 height: 65px;
                 position: relative;
        }
       .blankB {
                display: block;
                width: 465px;
                height: 55px;
                position: relative;
            }

        .header {
            font-size: 16px;
            font-weight: bold;
            color: blue;
            position: absolute;
            margin-left: 300px;
        }

        .content {
            margin-left: 100px;
            font-size: 12px;
            color: darkgray;
            position: absolute;
            top: 450px;
        }

        .content span {
            display: inline-block;
            width: 200px;
            margin-right: 10px;
            margin-top: 6px;
        }

        #ad {
            width: 365px;
        }

        .medicines {
            margin-left: 50px;
            font-size: 12px;
            color: darkgray;
            position: relative;
            top: 250px;
            left: 10x;
        }

        .medicines ol {
            margin: 0;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="blankA">

    </div>
    <div class="header">Dr. ${data.doctorLastName}, ${data.doctorFirstName}</div>
        <div class="blankB">

        </div>
    <div class="content">
        <span>${data.patientSSN}</span>${data.patientLastName}, ${data.patientFirstName}<br>
        <span>${data.patientAge}</span>${data.patientGender}<br>
        <span>${data.date}</span>${data.patientPhoneNum}<br>
        <span id="ad">${data.patientAddress}</span><br><br>
    </div>
    <div class="medicines">
        <ol>
            <#list data.medicines as medicine>
                <li>${medicine}</li>
            </#list>
        </ol>
    </div>
</div>
</body>
</html>
