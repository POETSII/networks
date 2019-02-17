def write_csv(file, content):
    """Write content (lists of lists) to CSV file."""
    with open(file, "w") as fid:
        lines_str = [map(str, line) for line in content]
        lines_csv = [", ".join(line) + "\n" for line in lines_str]
        fid.writelines(lines_csv)
