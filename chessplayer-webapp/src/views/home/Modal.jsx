import React from "react";
import PropTypes from 'prop-types';

export default function Modal({ buttonTitle, modalBody })
{
    // Return one modal, with the specified title and body text
    return (
        <div className="modalComponent">
            <button type="button" className="btn btn-secondary" data-bs-toggle="modal" data-bs-target={`#${buttonTitle}`}>
                {buttonTitle}
            </button>

            <div className="modal fade" id={buttonTitle} tabIndex="-1" aria-labelledby={`${buttonTitle}ModalLabel`} aria-hidden="true">
                <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                    <h5 className="modal-title" id={`${buttonTitle}ModalLabel`}>{buttonTitle}</h5>
                    <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />
                    </div>
                    <div className="modal-body">
                        {modalBody}
                    </div>
                    <div className="modal-footer">
                    <button type="button" className="btn btn-primary" data-bs-dismiss="modal">Got it!</button>
                    </div>
                </div>
                </div>
            </div>
        </div>
    );
}

Modal.propTypes = {
    buttonTitle: PropTypes.string.isRequired,
    modalBody: PropTypes.node.isRequired,
};
