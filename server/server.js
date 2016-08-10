var express = require('express');
var jade = require('jade');
var bodyParser = require('body-parser');
var app = express();
var path = require('path');
var jsonfile = require('jsonfile')
var mysql = require('mysql');
var connection = mysql.createConnection(
    {
        host: 'localhost',
        user: 'root',
        password: 'root',
        database: 'project',
    }
);

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(express.static(path.join(__dirname, 'public')));

connection.connect();

var city1 = "", city2 = "", day = ""
var dayArr = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']
app.use(bodyParser.urlencoded({extended: true}));

app.get('/', function (req, res) {
    res.render('index');
});

app.post('/resultsdroid', function (req, res) {

    FROM = req.body.from.toUpperCase();
    TO = req.body.to.toUpperCase();
    DATE = req.body.date;
    TIME = req.body.time;
    REQ = req;
    var SORT = req.body.sort;

    console.log(req.body.date)

    direct(FROM, TO, DATE, req, function (direct_result) {
        indirect(FROM, TO, DATE, req, function (indirect_result) {
            //console.log(direct_result)
            result = direct_result.concat(indirect_result);

            //console.log("result --------"+result);
            if (result.length != 0) {

                //filter unrealistic routes
                result.sort(function (a, b) {
                    return a.total_duration - b.total_duration;
                });
                var tenresult = [];
                for (var x = 0; x < 50; x++) {
                    if (x < result.length)
                        tenresult[x] = result[x];
                }
                result = tenresult

                var tempresult = [];
                var t = 0;
                for (var i = 0; i < result.length; i++) {
                    //console.log("wait_time"+(result[i].total_duration))
                    if ((result[i].leg)[0].arrival_start >= TIME) {
                        tempresult[t] = result[i];
                        tempresult[t].wait_time = parseInt((result[i].leg)[0].arrival_start) - parseInt(TIME)
                        //console.log("wait_time"+tempresult[t].wait_time)
                        t++;
                    }
                }

                result = tempresult;

                if (SORT == "1") {
                    result.sort(function (a, b) {
                        return a.total_duration - b.total_duration;
                    });
                }
                else if (SORT == "2") {
                    console.log("sort :::: 2");
                    result.sort(function (a, b) {
                        if ((a.leg)[0].arrival_start == (b.leg)[0].arrival_start)
                            return a.total_duration - b.total_duration;
                        else
                            return (a.leg)[0].arrival_start - (b.leg)[0].arrival_start;
                    });
                }
                else if (SORT == "3") {
                    result.sort(function (a, b) {
                        if (parseInt((a.leg)[a.leg.length - 1].day_def) >= parseInt((b.leg)[b.leg.length - 1].day_def)) {
                            console.log((a.leg)[a.leg.length - 1].arrival_end + ":::::" + (b.leg)[b.leg.length - 1].arrival_end)
                            if ((a.leg)[a.leg.length - 1].arrival_end == (b.leg)[b.leg.length - 1].arrival_end && a.day_def == b.day_def)
                                return a.total_duration - b.total_duration;
                            else
                                return (a.leg)[a.leg.length - 1].arrival_end - (b.leg)[b.leg.length - 1].arrival_end;
                        }
                        else
                            return false;
                    });
                }
                else if (SORT == "4") {
                    console.log("sort :::: 4");
                    result.sort(function (a, b) {
                        if (a.wait_time == b.wait_time)
                            return a.total_duration - b.total_duration;
                        else
                            return a.wait_time - b.wait_time;
                    });
                }

                tenresult = [];
                for (var x = 0; x < 10; x++) {
                    if (x < result.length)
                        tenresult[x] = result[x];
                }
                result = tenresult
            }

            //console.log(JSON.stringify(result))
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(result));
        });
    });
})

function direct(city1, city2, day, req, callback) {

    console.log(req.body.a1 + "       -" + req.body.a2)
    var classSearch = ' AND ('
    if (req.body.a1 == 'on' || req.body.a1 == 'true')
        classSearch = classSearch + "a.class LIKE '%1A%' OR "
    if (req.body.a2 == 'on' || req.body.a2 == 'true')
        classSearch = classSearch + "a.class LIKE '%2A%' OR "
    if (req.body.a3 == 'on' || req.body.a3 == 'true')
        classSearch = classSearch + "a.class LIKE '%3A%' OR "
    if (req.body.cc == 'on' || req.body.cc == 'true')
        classSearch = classSearch + "a.class LIKE '%CC%' OR "
    if (req.body.s2 == 'on' || req.body.s2 == 'true')
        classSearch = classSearch + "a.class LIKE '%2S%' OR "
    if (req.body.sl == 'on' || req.body.s1 == 'true')
        classSearch = classSearch + "a.class LIKE '%SL%' OR "
    if (req.body.e3 == 'on' || req.body.e3 == 'true')
        classSearch = classSearch + "a.class LIKE '%3E%' OR "
    if (req.body.fc == 'on' || req.body.fc == 'true')
        classSearch = classSearch + "a.class LIKE '%FC%' OR "
    if (req.body.gen == 'on' || req.body.gen == 'true')
        classSearch = classSearch + "a.class LIKE '%UNRESERVED%' OR a.class LIKE '%GN%' OR "

    classSearch = classSearch.substring(0, classSearch.length - 4) + ')'

    var date = new Date(day)
    day = dayArr[date.getDay()]
    var queryString = "select a.station_name as start,b.station_name as end,a.train_id,a.train_name,a.class,TIME_TO_SEC(a.arrival),TIME_TO_SEC(a.departure),TIME_TO_SEC(b.arrival),TIME_TO_SEC(b.departure),TIME_TO_SEC(b.arrival)+((b.arr_day-a.arr_day) *86400)-TIME_TO_SEC(a.departure) AS timeDef,(b.arr_day-a.arr_day) as dayDef from project.train a INNER JOIN project.train as b where a.train_id=b.train_id AND b.station_id=\"" + city2 + "\" AND a.station_id=\"" + city1 + "\" AND a.sno<b.sno AND (a.schedule LIKE '%Daily%' OR a.schedule LIKE '%" + day + "%') AND (a.route_no=b.route_no OR a.route_no=0) " + classSearch + " ORDER by a.train_id";
    console.log(queryString)
    connection.query(queryString, function (err, rows, fields) {
        if (err) {
            console.log(err + "\n\n" + queryString);
            callback('error ' + err);
        }
        else {
            if (rows.length != 0) {
                //console.log('City 1 is ' + city1 + ' City 2 is ' + city2)
                temp = []
                for (x = 0; x < rows.length; x++) {
                    //console.log(rows[x].train_id);
                    tempA = ({
                        train_id: rows[x].train_id,
                        train_name: rows[x].train_name,
                        train_class: rows[x].class,
                        station_id_start: city1,
                        station_id_end: city2,
                        station_name_start: rows[x].start,
                        station_name_end: rows[x].end,
                        arrival_start: rows[x]['TIME_TO_SEC(a.arrival)'],
                        departure_start: rows[x]['TIME_TO_SEC(a.departure)'],
                        arrival_end: rows[x]['TIME_TO_SEC(b.arrival)'],
                        departure_end: rows[x]['TIME_TO_SEC(b.departure)'],
                        day_def: rows[x].dayDef,
                    });
                    leg = []
                    leg[0] = tempA;
                    temp.push({
                        leg: leg,
                        total_duration: rows[x].timeDef,
                    });
                    //console.log("leg me up ::::::::"+(temp[0].leg)[0].train_id)
                }
                //console.log("temp ------------"+(temp[0].leg)[0].train_id);
                callback(temp);
            }
            else {
                console.log('No Result')
                callback([]);
            }
        }
    });
}

function indirect(city1, city2, day, req, callback) {

    console.log(req.body.depart_time)
    var classSearch = ' AND ('
    if (req.body.a1 == 'on' || req.body.a1 == 'true')
        classSearch = classSearch + "a.class LIKE '%1A%' OR "
    if (req.body.a2 == 'on' || req.body.a2 == 'true')
        classSearch = classSearch + "a.class LIKE '%2A%' OR "
    if (req.body.a3 == 'on' || req.body.a3 == 'true')
        classSearch = classSearch + "a.class LIKE '%3A%' OR "
    if (req.body.cc == 'on' || req.body.cc == 'true')
        classSearch = classSearch + "a.class LIKE '%CC%' OR "
    if (req.body.s2 == 'on' || req.body.s2 == 'true')
        classSearch = classSearch + "a.class LIKE '%2S%' OR "
    if (req.body.sl == 'on' || req.body.s1 == 'true')
        classSearch = classSearch + "a.class LIKE '%SL%' OR "
    if (req.body.e3 == 'on' || req.body.e3 == 'true')
        classSearch = classSearch + "a.class LIKE '%3E%' OR "
    if (req.body.fc == 'on' || req.body.fc == 'true')
        classSearch = classSearch + "a.class LIKE '%FC%' OR "
    if (req.body.gen == 'on' || req.body.gen == 'true')
        classSearch = classSearch + "a.class LIKE '%UNRESERVED%' OR a.class LIKE '%GN%' OR "

    classSearch = classSearch.substring(0, classSearch.length - 4) + ')'

    temps = []
    var date = new Date(day)
    day = dayArr[date.getDay()]
    var queryString = "SELECT DISTINCT a.station_id,b.station_name as start,a.train_id,a.train_name,a.class,a.schedule,TIME_TO_SEC(a.arrival),TIME_TO_SEC(a.departure),TIME_TO_SEC(b.arrival),TIME_TO_SEC(b.departure),TIME_TO_SEC(a.arrival) as arrivalCol,TIME_TO_SEC(a.arrival)+((a.arr_day-b.arr_day) *86400)-TIME_TO_SEC(b.departure) AS timeDef,(a.arr_day-b.arr_day) as dayDef1 from project.train as a INNER JOIN(SELECT * from project.train WHERE station_id=\"" + city1 + "\") as b where a.train_id=b.train_id AND a.sno>b.sno AND (b.schedule LIKE '%Daily%' OR b.schedule LIKE '%" + day + "%')AND (a.route_no=b.route_no OR a.route_no=0)" + classSearch + "";
    console.log(queryString)
    connection.query(queryString, function (err, rows, fields) {
        if (err) {
            console.log(err + "\n\n-----" + queryString);
            callback('error ' + err)
        }
        else {
            var queryString2 = "SELECT DISTINCT a.station_name as mid,b.station_name as end,a.station_id,a.train_id,a.train_name,a.class,a.schedule,TIME_TO_SEC(a.arrival),TIME_TO_SEC(a.departure),TIME_TO_SEC(b.arrival),TIME_TO_SEC(b.departure), TIME_TO_SEC(a.departure) as arrivalCol,TIME_TO_SEC(b.arrival)+((b.arr_day-a.arr_day) *86400)-TIME_TO_SEC(a.departure) AS timeDef,(b.arr_day-a.arr_day) as dayDef2 from project.train as a INNER JOIN(SELECT * from project.train WHERE station_id=\"" + city2 + "\") as b where a.train_id=b.train_id AND a.sno<b.sno AND (a.route_no=b.route_no OR a.route_no=0)" + classSearch + "";
            connection.query(queryString2, function (err, rows2, fields) {
                if (err) {
                    console.log(err + "\n\n-----" + queryString);
                    callback('error ' + err)
                }
                else {
                    var temp2 = 0, z = 0
                    console.log(queryString2)
                    for (x = 0; x < rows.length; x++) {
                        for (y = 0; y < rows2.length; y++) {
                            if (rows[x].station_id == rows2[y].station_id) {
                                //layover                                
                                var totalTime = rows[x].timeDef + rows2[y].timeDef
                                var day2 = dayArr[dayArr.indexOf(day) + rows[x].dayDef]
                                if (rows2[y].schedule.indexOf('Daily') > -1) {
                                    if (rows[x].arrivalCol < rows2[y].arrivalCol) {
                                        totalTime = totalTime + (rows2[y].arrivalCol - rows[x].arrivalCol)
                                    }
                                    else {
                                        totalTime = totalTime + 86400 - (rows[x].arrivalCol - rows2[y].arrivalCol)
                                    }
                                }
                                else {
                                    temp2 = dayArr.indexOf(day2)
                                    z = temp2
                                    while (true) {
                                        if (rows2[y].schedule.indexOf(dayArr[z % 7]) > -1) {
                                            if (rows[x].arrivalCol < rows2[y].arrivalCol) {
                                                totalTime = totalTime + ((z - temp2) * 86400) + (rows2[y].arrivalCol - rows[x].arrivalCol)
                                            }
                                            else {
                                                // console.log(totalTime+' '+z+'   '+temp2+'   '+rows[x].arrivalCol+'   '+rows2[y].arrivalCol)
                                                totalTime = totalTime + ((z - temp2 + 1) * 86400) - (rows[x].arrivalCol - rows2[y].arrivalCol)
                                            }
                                            break;
                                        }
                                        else {
                                            z++;
                                            //console.log(dayArr[z%7]);
                                            // console.log(rows2[y].train_id);
                                        }
                                    }
                                }
                                // totalTime = parseInt(totalTime/60/60)+':'+parseInt(totalTime/60%60);
                                //a & b are reversed for some reason in the first query
                                tempB = ({
                                    train_id: rows[x].train_id,
                                    train_name: rows[x].train_name,
                                    train_class: rows[x].class,
                                    day_def: rows[x].dayDef1,

                                    station_name_start: rows[x].start,
                                    station_name_end: rows2[y].mid,
                                    station_id_start: city1,
                                    station_id_end: rows[x].station_id,

                                    arrival_start: rows[x]['TIME_TO_SEC(b.arrival)'],
                                    departure_start: rows[x]['TIME_TO_SEC(b.departure)'],
                                    arrival_end: rows[x]['TIME_TO_SEC(a.arrival)'],
                                    departure_end: rows[x]['TIME_TO_SEC(a.departure)'],

                                });
                                tempC = ({
                                    train_id: rows2[y].train_id,
                                    train_name: rows2[y].train_name,
                                    train_class: rows2[y].class,
                                    day_def: (+rows2[y].dayDef2) + (+rows[x].dayDef1),//unary conversion to nos

                                    station_name_start: rows2[y].mid,
                                    station_name_end: rows2[y].end,
                                    station_id_start: rows[x].station_id,
                                    station_id_end: city2,

                                    arrival_start: rows2[y]['TIME_TO_SEC(a.arrival)'],
                                    departure_start: rows2[y]['TIME_TO_SEC(a.departure)'],
                                    arrival_end: rows2[y]['TIME_TO_SEC(b.arrival)'],
                                    departure_end: rows2[y]['TIME_TO_SEC(b.departure)'],
                                });
                                legs = []
                                legs[0] = tempB;
                                legs[1] = tempC;
                                temps.push({
                                    leg: legs,
                                    total_duration: totalTime,
                                })
                                //console.log("leg me up ::::::::"+(temps[0].leg)[1].train_id)
                                // console.log(rows[x].train_id+'    '+rows[x].station_id+'----------'+rows2[y].train_id+'    '+rows2[y].station_id+'    '+totalTime+'    '+z)
                            }
                        }
                    }
                    callback(temps);
                }
            });
        }
    });
}

app.listen(8080, function () {
    console.log('Server running at http://127.0.0.1:8080/');
});