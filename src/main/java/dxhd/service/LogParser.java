package dxhd.service;

import dxhd.model.LogEntry;

public interface LogParser {
    LogEntry parse(String logStr);
}
