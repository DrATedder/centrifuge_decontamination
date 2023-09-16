import os
import sys
import glob

def read_otu_list(env_sample):
    otu_list = set()
    for line in env_sample:
        otu = line.split("\t")[0]
        otu_list.add(otu)
    return otu_list

def read_genus_or_below_list(env_sample):
    otu_list = set()
    target_taxa = ["genus", "species", "subspecies", "leaf"]
    for line in env_sample:
        tax_rank = line.split("\t")[2].strip()
        otu = line.split("\t")[0]
        if tax_rank in target_taxa:
            if tax_rank == "leaf":
                otu_mod = f"{otu.split()[0]} {otu.split()[1]}"
                otu_list.add(otu_mod)
            else:
                otu_list.add(otu)
    return otu_list

def read_species_list(env_sample):
    otu_list = set()
    target_taxa = ["species", "subspecies", "leaf"]
    for line in env_sample:
        tax_rank = line.split("\t")[2].strip()
        otu = line.split("\t")[0].strip()
        if tax_rank in target_taxa:
            if tax_rank == "leaf":
                otu_mod = f"{otu.split()[0]} {otu.split()[1]}"
                otu_list.add(otu_mod)
            else:
                otu_list.add(otu)
    return otu_list

def create_env_list(env_sample, tax_level):
    if tax_level == "total":
        return read_otu_list(env_sample)
    elif tax_level == "genus":
        return read_genus_or_below_list(env_sample)
    elif tax_level == "species":
        return read_species_list(env_sample)
    else:
        raise ValueError("Invalid tax_level. Accepted values are 'total', 'genus', or 'species'.")

def decontaminate(samples_path, env_sample_path, tax_level):
    with open(env_sample_path, "r") as env_sample_file:
        env_list = create_env_list(env_sample_file, tax_level)
    output_file_path = "{0}_{1}_env_decontam_centrifugeReport.txt".format(os.path.splitext(samples_path)[0], tax_level)
    with open(samples_path, "r") as f_in, open(output_file_path, "w") as decontam_out:
        for line in f_in:
            if not line.startswith("name"):
                name = line.split("\t")[0]
                if name not in env_list:
                    decontam_out.write(line)
            else:
                decontam_out.write(line)
    return "Output completed"

def main():
    if len(sys.argv) != 5:
        print("Usage: python script.py <samples_folder> <bone_folder> <metadata_file> <tax_level>")
        sys.exit(1)

    samples_folder = sys.argv[1]
    bone_folder = sys.argv[2]
    metadata_file = sys.argv[3]
    tax_level = sys.argv[4]

    if tax_level not in ["total", "genus", "species"]:
        print("Error: Invalid tax_level. Accepted values are 'total', 'genus', or 'species'.")
        sys.exit(1)

    sample_dict = {}
    bone_dict = {}

    for file in glob.glob(os.path.join(samples_folder, "*decon_centrifugeReport.txt")):
        sample_dict[os.path.basename(file).split("_")[0]] = file

    for file in glob.glob(os.path.join(bone_folder, "*decon_centrifugeReport.txt")):
        bone_dict[os.path.basename(file).split("_")[0]] = file

    with open(metadata_file, "r") as meta_in:
        for line in meta_in:
            if not line.startswith("sample"):
                sample, bone = map(str.strip, line.split(","))
                if sample in sample_dict:
                    decontaminate(sample_dict[sample], bone_dict.get(bone), tax_level)

if __name__ == "__main__":
    main()

