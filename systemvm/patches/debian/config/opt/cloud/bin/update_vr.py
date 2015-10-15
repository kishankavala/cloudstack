#!/usr/bin/python

from ConfigParser import SafeConfigParser;
import tarfile,sys
import subprocess;
import shlex;
import logging
import datetime

manifest_config = SafeConfigParser();

def validate_ver(prev_ver):
	for line in open("/etc/cloudstack-release"):
		if "Cloudstack Release " + prev_ver in line:
			return True
		else:
			return False
 

def install_rpms():
	global manifest_config;
	file_no = 1;
	while (file_no < len(manifest_config.options("packages"))):
		pkg = manifest_config.get("packages", "FILE"+str(file_no));
		logging.info("Installing " + pkg)
		subprocess.call(str("rpm -Uvh "+pkg).split());
		file_no = file_no+1;

def exec_commands(section):
	global manifest_config;
	cmd_no = 1;
	while (cmd_no < len(manifest_config.options(section))):
		cmd = manifest_config.get(section, "CMD"+str(cmd_no));
		subprocess.call(shlex.split(cmd));
		cmd_no = cmd_no+1;

def untar(fname):
    if (tarfile.is_tarfile(fname)):
        tar = tarfile.open(fname)
        tar.extractall(path="update")
        tar.close()
    else:
        logging.error("Not a tar file: '%s '" % sys.argv[1])
        sys.exit(1)

def update_next_ver(next_ver):
	ts = datetime.datetime.now().strftime("%a %b %d %T %Z %Y")
	f = open("/etc/cloudstack-release", "w")
	f.write("Cloudstack Release " + next_ver + " " + ts + "\n")
        f.close

if len(sys.argv) < 2:
	print "Usage: '%s tar filename'" % sys.argv[0]
        sys.exit(1)
untar(sys.argv[1])
cmdargs = str(sys.argv)
manifest_config.read("manifest.nfo");
prev_ver = manifest_config.get("general", "PREV_VERSION");
next_ver = manifest_config.get("general", "NEXT_VERSION");
logging.info("Updating from "+prev_ver+" to "+next_ver)
if not validate_ver(prev_ver):
        if validate_ver(next_ver):
		logging.info("Already at latest version nothing to do")
        	sys.exit(0)
	logging.error("Source version mismatch")
        sys.exit(1)
exec_commands("pre_install");
install_rpms();
exec_commands("post_install");
update_next_ver(next_ver)
print next_ver
sys.exit(0)
