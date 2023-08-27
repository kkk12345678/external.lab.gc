import React, { useState, useEffect } from "react";
import axios from "axios";
import {
    MenuItem,
    Pagination,
    Paper,
    Select,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    Box,
    Typography,
    Modal,
    FormGroup,
    TextField,
    Dialog,
    DialogTitle,
    DialogContentText,
    DialogContent,
    DialogActions, TableSortLabel
} from "@mui/material";
import "../css/certificates.css";
import {buttonStyle, style} from "../components/style";
import Header from "../components/header";

const jwt = window.localStorage.getItem("jwt");

const GiftCertificates = () => {
    const [totalRecords] = useState(0);
    const [certificatesList, setCertificatesList] = useState([]);
    const [controller, setController] = useState({limit: 10, page: 1});
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [limit, setLimit] = useState(10);
    const [searchString, setSearchString] = useState("");
    const [searchInput, setSearchInput] = useState("");
    const [sort, setSort] = useState({date: "asc", name: ""});

    useEffect(() => {
        const getCertificates = () => {
            let url = `http://localhost:8080/gift-certificates?sort=date,${sort.date}&limit=${controller.limit}&page=${controller.page}`;
            if (searchString.length > 0) {
                url += `&search=name,${searchString}`;
            }
            if (sort.name !== "") {
                url+= `&sort=name,${sort.name}`;
            }
            console.log(url);
            axios.get(url)
                .then((response) => setCertificatesList(response.data))
                .catch((error) => console.log(error));
        };
        getCertificates();
    }, [controller, searchString, sort]);

    useEffect(() => {
        let url = `http://localhost:8080/gift-certificates/count`;
        const getTotalPages = () => {
            if (searchString.length > 0) {
                url += `?search=name,${searchString}`;
            }
            axios.get(url)
                .then((response) => setTotalPages(Math.ceil(response.data / controller.limit)))
                .catch((error) => console.log(error));
        }
        getTotalPages();
    });

    useEffect(() => {
        setController(JSON.parse(window.localStorage.getItem('controller')));
        setCertificatesList(JSON.parse(window.localStorage.getItem('certificates')));
        setTotalPages(JSON.parse(window.localStorage.getItem('total-pages')));
        setLimit(JSON.parse(window.localStorage.getItem('limit')));
        setCurrentPage(JSON.parse(window.localStorage.getItem('current-page')));
    }, []);

    useEffect(() => {
        window.localStorage.setItem('controller', JSON.stringify(controller));
    }, [controller]);

    useEffect(() => {
        window.localStorage.setItem('certificates', JSON.stringify(certificatesList));
    }, [certificatesList]);

    useEffect(() => {
        window.localStorage.setItem('total-pages', JSON.stringify(totalPages));
    }, [totalPages]);

    useEffect(() => {
        window.localStorage.setItem('limit', JSON.stringify(limit));
    }, [limit]);

    useEffect(() => {
        window.localStorage.setItem('current-page', JSON.stringify(currentPage));
    }, [currentPage]);



    const [openView, setOpenView] = useState(false);

    const [openEditForm, setOpenEditForm] = useState(false);


    const [certificateId, setCertificateId] = useState(0);
    const [certificateName, setCertificateName] = useState("");
    const [certificateDescription, setCertificateDescription] = useState("");
    const [certificatePrice, setCertificatePrice] = useState(0);
    const [certificateDuration, setCertificateDuration] = useState(0);
    const [certificateTags, setCertificateTags] = useState("");
    const [certificateCreateDate, setCertificateCreateDate] = useState("");
    const [certificateLastEditDate, setCertificateLastEditDate] = useState("");

    const editCertificate = (event, certificate) => {
        setCurrentCertificate(certificate);
        setOpenEditForm(true);
    }

    const setCurrentCertificate = (certificate) => {
        setCertificateId(certificate.id);
        setCertificateName(certificate.name);
        setCertificateDescription(certificate.description);
        setCertificatePrice(certificate.price);
        setCertificateDuration(certificate.duration);
        setCertificateTags(certificate.tags.map(tag => tag.name).join(","));
        setCertificateCreateDate((new Date(certificate.createDate * 1000)).toDateString());
        setCertificateLastEditDate((new Date(certificate.lastUpdateDate * 1000)).toDateString());
    }

    const viewCertificate = (event, certificate) => {
        setCurrentCertificate(certificate);
        setOpenView(true);
    }

    const handleEditSave = () => {
        axios.put(`http://localhost:8080/gift-certificates/${certificateId}`, {
            name: certificateName,
            description: certificateDescription,
            tags: certificateTags.split(","),
            price: certificatePrice,
            duration: certificateDuration
        }, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt")
            }
        }).then((res) => {
            if (res.status === 200) {
                setOpenEditForm(false);
                window.location.reload();
            } else {
                console.log(res);
            }
        }).catch((error) => console.log(error));
    }

    const deleteCertificate = (event, id) => {
        axios.delete(`http://localhost:8080/gift-certificates/${id}`, {
            headers: {
                "Authorization": "Bearer " + jwt
            }
        }).then((res) => {
            if (res.status === 204) {
                window.location.reload();
            } else {
                console.log(res);
            }
        }).catch((error) => console.log(error))
    }

    const handleLimitChange = (e) => {
        console.log("Selected limit: ", e.target.value);
        setTotalPages(Math.ceil(totalRecords / e.target.value))
        setController({limit: e.target.value, page: 1})
    }

    const handlePageChange = (e, page) => {
        setController({limit: controller.limit, page: page});
    }

    const [openDeleteDialogue, setOpenDeleteDialogue] = useState(false);

    const changeSort = (column) => {
        if (column === "date") {
            setSort({date: (sort.date === "asc") ? "desc" : "asc", name: sort.name});
        }
        if (column === "name") {
            setSort({date: sort.date, name: (sort.name === "asc") ? "desc" : "asc"});
        }
    }

    return (
        <>
            <Header/>

            <Box sx={{display: "flex", justifyContent: "center"}}>
                <TextField
                    value={searchInput}
                    onChange={e => setSearchInput(e.target.value)}
                    sx={{width: "60%", marginTop: 4, marginBottom: 4}}
                    InputProps={{endAdornment: <Button onClick={() => setSearchString(searchInput)}>Search</Button>}}
                />
            </Box>

            <TableContainer sx={{alignContent:'center'}} component={Paper}>
                <Table sx={{width: '100%', alignContent:'center'}} aria-label="simple table">
                    <TableHead>
                        <TableRow sx={{ backgroundColor: "lightblue"}}>
                            <TableCell sx={{width: "10%"}}>
                                <TableSortLabel
                                    active={sort.date !== ""}
                                    direction={sort.date === "" ? "asc" : sort.date}
                                    onClick={() => changeSort("date")}
                                >
                                </TableSortLabel>
                                Create Date
                            </TableCell>
                            <TableCell sx={{width: "20%"}}>
                                <TableSortLabel
                                    active={sort.name !== ""}
                                    direction={sort.name === "" ? "asc" : sort.name}
                                    onClick={() => changeSort("name")}
                                >
                                </TableSortLabel>
                                Title
                            </TableCell>
                            <TableCell sx={{width: "20%"}}>Tags</TableCell>
                            <TableCell sx={{width: "20%"}}>Description</TableCell>
                            <TableCell sx={{width: "10%"}}>Price</TableCell>
                            <TableCell sx={{width: "20%"}}>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {certificatesList.map((certificate) => (
                            <TableRow
                                key={certificate.id}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 }}}
                            >
                                <TableCell sx={{padding: 1}}>{(new Date(certificate.createDate * 1000)).toDateString()}</TableCell>
                                <TableCell sx={{padding: 1}}>{certificate.name}</TableCell>
                                <TableCell sx={{padding: 1}}>{certificate.tags.map((tag) => tag.name).join(",")}</TableCell>
                                <TableCell sx={{padding: 1}}>{certificate.description}</TableCell>
                                <TableCell sx={{padding: 1}} align="right">{Number.parseFloat(certificate.price).toFixed(2)}</TableCell>
                                <TableCell sx={{padding: 1}}>
                                    <div className="div-buttons">
                                        <Button sx={buttonStyle} variant="contained" color="success" className="button-view" onClick={event => viewCertificate(event, certificate)}>View</Button>
                                        <Button sx={buttonStyle} variant="contained" className="button-edit" onClick={event => editCertificate(event, certificate)}>Edit</Button>
                                        <Button sx={buttonStyle} variant="contained" color="error" className="button-delete" onClick={() => setOpenDeleteDialogue(true)}>Delete</Button>
                                        <Dialog
                                            open={openDeleteDialogue}
                                            onClose={() => setOpenDeleteDialogue(false)}
                                            aria-labelledby="alert-dialog-title"
                                            aria-describedby="alert-dialog-description"
                                        >
                                            <DialogTitle id="alert-dialog-title">{"Confirmation"}</DialogTitle>
                                            <DialogContent>
                                                <DialogContentText id="alert-dialog-description">
                                                    Are you sure you want to delete the gift certificate?
                                                </DialogContentText>
                                            </DialogContent>
                                            <DialogActions>
                                                <Button onClick={() => setOpenDeleteDialogue(false)} autoFocus>No</Button>
                                                <Button onClick={event => deleteCertificate(event, certificate.id)}>Yes</Button>
                                            </DialogActions>
                                        </Dialog>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Modal
                open={openEditForm}
                onClose={() => setOpenEditForm(false)}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">Edit gift certificate</Typography>
                    <FormGroup sx={{marginBottom: 4}}>
                        <TextField sx={{marginTop: 4}} id="input-edit-name" label="Name" value={certificateName}  onChange={e => setCertificateName(e.target.value)}/>
                        <TextField sx={{marginTop: 4}} id="input-edit-description" label="Description" multiline={true} minRows={5} maxRows={5} onChange={e => setCertificateDescription(e.target.value)} value={certificateDescription}/>
                        <TextField sx={{marginTop: 4}} id="input-edit-tags" label="Tags" value={certificateTags} onChange={e => setCertificateTags(e.target.value)}/>
                        <TextField sx={{marginTop: 4}} id="input-edit-price" label="Price" type="number" step="0.01" value={certificatePrice} onChange={e => setCertificatePrice(Number(e.target.value))}/>
                        <TextField sx={{marginTop: 4}} id="input-edit-duration" label="Duration" type="number" value={certificateDuration} onChange={e => setCertificateDuration(Number(e.target.value))}/>
                    </FormGroup>
                    <div className={"div-edit-modal-buttons"}>
                        <Button sx={buttonStyle} variant="contained" color="success" onClick={handleEditSave}>Save</Button>
                        <Button sx={buttonStyle} variant="contained" color="error" onClick={() => setOpenEditForm(false)}>Cancel</Button>
                    </div>
                </Box>
            </Modal>
            <Modal
                open={openView}
                onClose={() => setOpenView(false)}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">View gift certificate</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Name: {certificateName}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Description: {certificateDescription}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Price: {certificatePrice}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Duration: {certificateDuration}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Tags: {certificateTags}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Create date: {certificateCreateDate}</Typography>
                    <Typography id="modal-modal-title" variant="h6" component="div">Last update date: {certificateLastEditDate}</Typography>
                    <div className={"div-edit-modal-buttons"}>
                        <Button sx={buttonStyle} variant="contained" color="success" onClick={() => setOpenView(false)}>Close</Button>
                    </div>
                </Box>
            </Modal>
            <div className={"div-footer"}>
                <div className={"div-pagination"}>
                    <Pagination
                        count={totalPages}
                        variant="outlined"
                        shape="rounded" size="large"
                        color="primary"
                        onChange={handlePageChange}
                    >
                    </Pagination>
                </div>
                <div className={"div-select"}>
                    <Select defaultValue={10} onChange={handleLimitChange}>
                        <MenuItem value={10}>10</MenuItem>
                        <MenuItem value={20}>20</MenuItem>
                        <MenuItem value={50}>50</MenuItem>
                    </Select>
                </div>
            </div>
        </>
    )
}

export default GiftCertificates;