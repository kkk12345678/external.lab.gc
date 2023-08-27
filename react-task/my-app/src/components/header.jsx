import React, {useState} from "react";
import {AppBar, Box, Button, FormGroup, IconButton, Modal, TextField, Toolbar, Typography} from "@mui/material";
import axios from "axios";
import {buttonStyle, style} from "./style";

const Header = () => {
    const [openNewForm, setOpenNewForm] = useState(false);
    const handleNewFormOpen = () => setOpenNewForm(true);
    const handleNewFormClose = () => setOpenNewForm(false);

    const logout = () => {
        window.localStorage.removeItem("jwt");
        window.location.replace("/login");
    }

    const handleAddSave = () => {
        axios.post("http://localhost:8080/gift-certificates", {
            name: document.getElementById("input-name").value,
            description: document.getElementById("input-description").value,
            tags: document.getElementById("input-tags").value.split(","),
            price: document.getElementById("input-price").value,
            duration: document.getElementById("input-duration").value
        }, {
            headers: {
                "Authorization": "Bearer " + window.localStorage.getItem("jwt")
            }
        }).then((res) => {
            if (res.status === 201) {
                setOpenNewForm(false);
                window.location.reload();
            } else {
                console.log(res);
            }
        }).catch((error) => console.log(error));
    }

    return (
        <>
            <Box sx={{flexGrow: 1, marginBottom: 5, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{mr: 2}}
                        >
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                            <Button color="inherit" onClick={handleNewFormOpen}>Add new</Button>
                        </Typography>
                        <Button color="inherit" onClick={logout}>Logout</Button>
                    </Toolbar>
                </AppBar>
            </Box>
            <Modal
                open={openNewForm}
                onClose={handleNewFormClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">Add new gift certificate</Typography>
                    <FormGroup sx={{marginBottom: 4}}>
                        <TextField sx={{marginTop: 4}} id="input-name" label="Name"/>
                        <TextField sx={{marginTop: 4}} id="input-description" label="Description" multiline={true}
                                   minRows={5} maxRows={5}/>
                        <TextField sx={{marginTop: 4}} id="input-tags" label="Tags"/>
                        <TextField sx={{marginTop: 4}} id="input-price" label="Price" type="number" step="0.01"/>
                        <TextField sx={{marginTop: 4}} id="input-duration" label="Duration" type="number"/>
                    </FormGroup>
                    <div className={"div-edit-modal-buttons"}>
                        <Button sx={buttonStyle} variant="contained" color="success"
                                onClick={handleAddSave}>Save</Button>
                        <Button sx={buttonStyle} variant="contained" color="error"
                                onClick={handleNewFormClose}>Cancel</Button>
                    </div>
                </Box>
            </Modal>
        </>
    );
}

export default Header;
