package dxhd.service;

import dxhd.utils.LogBuffer;

public interface LogReader {
    void readIntoBuffer(LogBuffer logBuffer);
}
