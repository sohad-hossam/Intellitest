import { useEffect } from "react";
import "./HomePage/HomePage.css";
import "bootstrap/dist/css/bootstrap.min.css";
import '@fortawesome/fontawesome-free/css/all.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { Header } from "./TopBar/TopBar";
import { PageTitle } from "./PageTitle/PageTitle";
import "./AboutUs.css";
import Group from './assets/group.png';
import image1 from './assets/beso.jpeg';
import image2 from './assets/yasmin.jpeg';
import image3 from './assets/yasmeen.jpeg';
import image4 from './assets/sohad.jpeg';


let progress = -1;

function animate() {
  const body = document.querySelector(".HomePage");
  if (body != null) {
    // Apply translate values to the box
    body.style.backgroundImage = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 2000 2500'%3E%3Cdefs%3E%3CradialGradient id='a' gradientUnits='objectBoundingBox'%3E%3Cstop offset='0' stop-color='%235C8374'/%3E%3Cstop offset='1' stop-color='%23092635'/%3E%3C/radialGradient%3E%3ClinearGradient id='b' gradientUnits='userSpaceOnUse' x1='0' y1='750' x2='1550' y2='750'%3E%3Cstop offset='0' stop-color='%23335555'/%3E%3Cstop offset='1' stop-color='%23092635'/%3E%3C/linearGradient%3E%3Cpath id='s' fill='url(%23b)' d='M1549.2 51.6c-5.4 99.1-20.2 197.6-44.2 293.6c-24.1 96-57.4 189.4-99.3 278.6c-41.9 89.2-92.4 174.1-150.3 253.3c-58 79.2-123.4 152.6-195.1 219c-71.7 66.4-149.6 125.8-232.2 177.2c-82.7 51.4-170.1 94.7-260.7 129.1c-90.6 34.4-184.4 60-279.5 76.3C192.6 1495 96.1 1502 0 1500c96.1-2.1 191.8-13.3 285.4-33.6c93.6-20.2 185-49.5 272.5-87.2c87.6-37.7 171.3-83.8 249.6-137.3c78.4-53.5 151.5-114.5 217.9-181.7c66.5-67.2 126.4-140.7 178.6-218.9c52.3-78.3 96.9-161.4 133-247.9c36.1-86.5 63.8-176.2 82.6-267.6c18.8-91.4 28.6-184.4 29.6-277.4c0.3-27.6 23.2-48.7 50.8-48.4s49.5 21.8 49.2 49.5c0 0.7 0 1.3-0.1 2L1549.2 51.6z'/%3E%3Cg id='g'%3E%3Cuse href='%23s' transform='scale(0.12) rotate(60)'/%3E%3Cuse href='%23s' transform='scale(0.2) rotate(10)'/%3E%3Cuse href='%23s' transform='scale(0.25) rotate(40)'/%3E%3Cuse href='%23s' transform='scale(0.3) rotate(-20)'/%3E%3Cuse href='%23s' transform='scale(0.4) rotate(-30)'/%3E%3Cuse href='%23s' transform='scale(0.5) rotate(20)'/%3E%3Cuse href='%23s' transform='scale(0.6) rotate(60)'/%3E%3Cuse href='%23s' transform='scale(0.7) rotate(10)'/%3E%3Cuse href='%23s' transform='scale(0.835) rotate(-40)'/%3E%3Cuse href='%23s' transform='scale(0.9) rotate(40)'/%3E%3Cuse href='%23s' transform='scale(1.05) rotate(25)'/%3E%3Cuse href='%23s' transform='scale(1.2) rotate(8)'/%3E%3Cuse href='%23s' transform='scale(1.333) rotate(-60)'/%3E%3Cuse href='%23s' transform='scale(1.45) rotate(-30)'/%3E%3Cuse href='%23s' transform='scale(1.6) rotate(10)'/%3E%3C/g%3E%3C/defs%3E%3Cg transform='translate(100 0)'%3E%3Cg %3E%3Ccircle fill='url(%23a)' r='3000'/%3E%3Cg opacity='0.5'%3E%3Ccircle fill='url(%23a)' r='2000'/%3E%3Ccircle fill='url(%23a)' r='1800'/%3E%3Ccircle fill='url(%23a)' r='1700'/%3E%3Ccircle fill='url(%23a)' r='1651'/%3E%3Ccircle fill='url(%23a)' r='1800'/%3E%3Ccircle fill='url(%23a)' r='1250'/%3E%3Ccircle fill='url(%23a)' r='1175'/%3E%3Ccircle fill='url(%23a)' r='900'/%3E%3Ccircle fill='url(%23a)' r='1250'/%3E%3Ccircle fill='url(%23a)' r='500'/%3E%3Ccircle fill='url(%23a)' r='380'/%3E%3Ccircle fill='url(%23a)' r='250'/%3E%3C/g%3E%3Cg transform='rotate(${progress} 0 0)'%3E%3Cuse href='%23g' transform='rotate(10)'/%3E%3Cuse href='%23g' transform='rotate(120)'/%3E%3Cuse href='%23g' transform='rotate(240)'/%3E%3C/g%3E%3Ccircle fill-opacity='0' fill='url(%23a)' r='3000'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E ")  `;
    // Update progress for next frame
    progress = progress - 0.35;
    // Repeat the animation until progress reaches 100
    if (progress >= -360) {
      requestAnimationFrame(animate);
    } else {
      progress = -1; // Reset progress for next animation loop
      requestAnimationFrame(animate);
    }
  }
}

export function AboutUs() {
  useEffect(() => {
    animate();
  }, []);
  const visibleHyperlinks = [
    "Home",
    "Proceed With",
    "About Us",
  ];
  return (
    <div className="HomePage">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="About Us" />
      <div className="mt-5"></div>
      <div className="container">
        <div className="row">
          <div className="col-6">
            <div className="about-us-content">
              <div className="question">Who are we ?</div>
              <p>
                We are a team of 4 students from the Cairo University, Egypt. We are currently in our 4th year of study and are
                working on our graduation project "INTLITEST". Our project is to nsha3bol el 2omoor kda l7d ma ne3raf elbeta3 da by3ml eh 
                3lshan 3ndi currectly "azmet fokdan el haweya el mashr3oya" wmsh fay2a lel mas5ara w elt el adab de.
              </p>
            </div>
          </div>
          <div className="col-1"></div>
          <div className="col-4">
            <div className="circular-parent-container">
              <div className="circular-container">
                <img src={Group} alt="mockup" className="circular-image" />
              </div>
            </div>
          </div>
        </div>

        <div className="row">
          <div className="col-3 card-wrapper">
            <div className="card">
            <div className="card-name">
                <b>Bassant Hisham</b>
              </div>
              <img src={image1} alt="Card 1" className="card-image" />
              <div className="card-content">
              <p>Hello, this is Beso, soon to be graduated Beso <br/> يوما ما زيتونه هتحتل العالم</p>
              </div>
            </div>
          </div>
          <div className="col-3 card-wrapper">
            <div className="card">
            <div className="card-name">
                <b>Yasmin Hashem</b>
              </div>
              <img src={image2} alt="Card 2" className="card-image" />
              <div className="card-content">
                <p>Hello, this is Yasmin, soon to be graduated Yasmin <br/> يوما ما هحتل العالم</p>
              </div>
            </div>
          </div>
          <div className="col-3 card-wrapper">
            <div className="card">
            <div className="card-name">
                <b>Yasmeen Zaki</b>
              </div>
              <img src={image3} alt="Card 3" className="card-image" />
              <div className="card-content">
              <p>Hello, this is Yasmin with an e, soon to be graduated Yasmin <br/> يوما ما هحتل العالم</p>
              </div>
            </div>
          </div>
          <div className="col-3 card-wrapper">
            <div className="card">
            <div className="card-name">
                <b>Sohad Hossam</b>
              </div>
              <img src={image4} alt="Card 4" className="card-image" />
              <div className="card-content">
              <p>Hello, this is Sohad, soon to be graduated Sohad <br/> هحتل العالم بكره الصبح</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="PageBottom" style={{ width: '100%', backgroundColor: '#092635', minHeight: '50px', padding: '20px 0' }}>
    <div className="container">
        <div className="row">
            <div className="col-md-6">
                <h2 style={{ color: '#ffffff', fontWeight: 'bold' }}>Contact Us</h2>
                <ul className="list-inline">
                    <li className="list-inline-item"><a href="mailto:bassentkhafagi@gmail.com"><i className="fab fa-google fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="mailto:yasmin.hashem201@gmail.com"><i className="fab fa-google fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="mailto:yasmeen.zaki01@gmail.com"><i className="fab fa-google fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="mailto:sohad95husam@gmail.com"><i className="fab fa-google fa-2x text-white"></i></a></li>
                </ul>
               
            </div>
            <div className="col-md-6">
                <h2 style={{ color: '#ffffff', fontWeight: 'bold' }}>Follow Us</h2>
                <ul className="list-inline">
                    <li className="list-inline-item"><a href="https://www.linkedin.com/in/bassant-hisham-609ab0162/"><i className="fab fa-linkedin fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="https://www.linkedin.com/in/yasmin-hashem-734089218/"><i className="fab fa-linkedin fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="https://www.linkedin.com/in/yasmeen-zaki/"><i className="fab fa-linkedin fa-2x text-white"></i></a></li>
                    <li className="list-inline-item"><a href="https://www.linkedin.com/in/sohad-hossam-994aa8237/"><i className="fab fa-linkedin fa-2x text-white"></i></a></li>
                </ul>
            </div>
        </div>
        <div className="mt-3">
    <h3>
        <a href="https://docs.google.com/document/d/1kU-c8wsEUrNs8FKT7EbB6aFeMTJcEd2E/edit" className="text-decoration-none text-white">
            Get deeper insights of our product through the book
        </a>
    </h3>
</div>


        <div className="mt-4 text-end">
            <p style={{ color: '#ffffff' }}>© 2024 INTLLITEST. All Rights Reserved.</p>
        </div>
    </div>
</div>

    </div>
  
  );
}
