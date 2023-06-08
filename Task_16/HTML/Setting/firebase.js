// Them thu vien
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.18.0/firebase-app.js";
import { getDatabase, ref, set, child, get, onValue, update} from "https://www.gstatic.com/firebasejs/9.18.0/firebase-database.js";

// Cau hinh firebase
const firebaseConfig = {
    apiKey: "AIzaSyCpPpRkjFtYINVc2QJSimkbCtGIe5zQUrM",
    authDomain: "dieu-khien-nhiet-do-dixell.firebaseapp.com",
    databaseURL: "https://dieu-khien-nhiet-do-dixell-default-rtdb.firebaseio.com",
    projectId: "dieu-khien-nhiet-do-dixell",
    storageBucket: "dieu-khien-nhiet-do-dixell.appspot.com",
    messagingSenderId: "961902819526",
    appId: "1:961902819526:web:cf034f57e9acac8bfa7fc7",
    measurementId: "G-RWC55H8W3Z"
  };

// Khai bao bien  
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);
const db = getDatabase();

var low_temp = 0;
var high_temp = 0;
var temp = 0;
var measurement_time = 0;

const ref_tempMin = ref(db, 'Setting/tempMin');
onValue(ref_tempMin, (snapshot) => {
    low_temp = snapshot.val();
    document.getElementById('low_temp').value = low_temp;
});

const ref_tempMax = ref(db, 'Setting/tempMax');
onValue(ref_tempMax, (snapshot) => {
    high_temp = snapshot.val();
    document.getElementById('high_temp').value = high_temp;
});

const ref_temp = ref(db, 'Setting/temp');
onValue(ref_tempMin, (snapshot) => {
    temp = snapshot.val();
    document.getElementById('temp').value = temp;
});

const ref_time = ref(db, 'Setting/time');
onValue(ref_time, (snapshot) => {
    measurement_time = snapshot.val();
    document.getElementById('measurement_time').value = measurement_time;
});