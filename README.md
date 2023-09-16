# centrifuge_decontamination
A simple Java-based GUI (python backend) to run OTU decontamination on centrifuge (metagenomic) output files.

Decontamination of ancient DNA (aDNA) samples (or indeed, any metagenomic samples) is really important. If you have access to either a sequenced 'lab blank' and/or an environmental sample (for example a bone sample from the same burial site if you are working on ancient oral microbiomes), you will be able to taxonomically identify OTUs which may otherwise be erroneously attributed to your aDNA sample.

## Appearance
![Screenshot](https://github.com/DrATedder/centrifuge_decontamination/blob/3d5563b3284749bce953be784f425579392551de/centrifuge_decontamination.png "Image of centrifuge_decontamination App")

## Features

- **User-Friendly Interface:** The app offers a simple and intuitive GUI for inputting data and parameters.

- **Browse for Files:** Easily select folders and files by using the "Browse" buttons for samples, contaminants, and metadata.

- **Decontamination Options:** Choose the desired taxonomic level for decontamination: "total," "genus," or "species."

- **Decontamination Execution:** Execute the Python script with user-defined inputs and view the output in real-time within the app.


## Usage requirements

Before using this application, ensure you have the following installed:

- Java Runtime Environment (JRE)
- Python (with required dependencies for the Python script)
- Centrifuge (Not required for the script, but necessary for generation of required input files)

## Usage

1. Clone or download this repository to your local machine.

2. Compile the Java code:

   ```bash
   javac centrifuge_decontamination.java

3. Run the Java application:

    ```bash

    java centrifuge_decontamination

4. Use the app to browse for input folders and files, select the taxonomic level, and click the "Decontaminate" button to start the decontamination process.

## Further input parameter details

**Prerequisites:** All samples ('real' and 'contaminants') should have been run through centrifuge to produce 'centrifugeReport.txt' files.

**Requirements:**

1.    directory containing sample files (*centrifugeReport.txt format; see below for naming protocols)
2.    directory containing either contaminents (*centrifugeReport.txt format; can be the same folder as the samples are given in)
3.    metadata file (CSV format, see below for details)
4.    taxonomic level (either 'total', 'genus' or 'species'; see below for explanation)

**File naming protocol:** Centrifuge output files should be named in the following manner:

> shortname_anything_centrifugeReport.txt

1.    shortname: used to link files to the metadata
2.    anything: not used, but can be anything
3.    centrifugeReport.txt: used by the programme to identify the correct files within the given directory
4.    underscores ('_') must be used between file name elements as these are used for splitting file names

**Metadata format:** Metadata should be in two column CSV format as shown below (example can be downloaded [here](https://github.com/DrATedder/centrifuge_decontamination/blob/3d5563b3284749bce953be784f425579392551de/decontamination_metadata_example.csv "Example metadata file")). The first column should contain the sequence 'shortname' for each file you want to process, and the second column should contain the sequence 'shortname' for the contaminant file. Note. If either file (sample or contaminent) is in the metadata but not in the directories given, they will be ignored.
|sample|contaminent|
|---|---|
|ERR9638263|ERR9638259|
|ERR9638253|ERR9638262|
|ERR9638254|ERR9638262|
|ERR9638255|ERR9638262|
|ERR9638256|ERR9638262|

**Taxonomic level:** Taxonomic level explains what OTUs from the contaminant sample will be reomved from the 'real' sample. Brief explanations for these are given below:

*total* - This will remove any OTUs which overlap at any taxRank level. This is likely to be super conservative, and may only be useful in certain circumstance.

*genus* - This will remove overlapping OTUs from the genus level down (inc. 'genus', 'species', 'subspecies' & 'leaf').

*species* - This will remove overlapping OTUs from the species level down (inc. 'species', 'subspecies' & 'leaf').

**Output files:** Output file, still in 'centrifugeReport.txt' format will be output into the directory containing the samples. File names will have been appended in the following way:

> shortname_anything_<tax_level>_decontam_centrifugeReoprt.txt

To integrate this process into a bash/python pipeline, use the python backend 'centrifuge_env_decontam.py'. Full details for this can be found [here](https://github.com/DrATedder/ancient_metagenomics "Link to ancient_metagenomics github page.").

## Author

    Dr. Andrew Tedder
    University of Bradford

## License

This project is licensed under the MIT License - see the LICENSE file for details.
