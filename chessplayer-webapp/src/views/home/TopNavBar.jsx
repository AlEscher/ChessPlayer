import React from 'react';
import Modal from './Modal';

const aboutModalBody = (
<ul>
    <li>
        Collaborators
        <ul>
            <li><a href="https://github.com/AlEscher">AlEscher</a></li>
        </ul>
    </li>
    <li>
        Frameworks / APIs used
        <ul>
                <li>
        <a href="https://reactjs.org/">ReactJS</a>
        ,
        {' '}
        <a href="https://getbootstrap.com/">Bootstrap</a>
        {' '}
        for WebApp
                </li>
                <li>
        <a href="https://spring.io/">Java Spring</a>
        {' '}
        for Webserver
                </li>
        </ul>
    </li>
</ul>
);

export default function TopNavBar()
    {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="/">ChessPlayer</a>
                    <button
                      className="navbar-toggler"
                      type="button"
                      data-bs-toggle="collapse"
                      data-bs-target="#navbarSupportedContent"
                      aria-controls="navbarSupportedContent"
                      aria-expanded="false"
                      aria-label="Toggle navigation"
                    >
                        <span className="navbar-toggler-icon" />
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <a className="nav-link" href="https://github.com/AlEscher/ChessPlayer">Source</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="https://www.linkedin.com/in/alessandro-escher/">Author</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="https://www.soundsnap.com/tags/chess">Sounds</a>
                            </li>
                        </ul>
                        <ul className="navbar-nav mb-2 mb-lg-0">
                            <li className="nav-item">
                                <Modal buttonTitle="About" modalBody={aboutModalBody} />
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        );
    }
