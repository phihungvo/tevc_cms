import React from 'react';
import { Table as AntTable } from 'antd';
import classNames from 'classnames/bind';
import styles from './SmartTable.module.scss';

const cx = classNames.bind(styles);

function SmartTable({
                        columns,
                        dataSources,
                        loading,
                        pagination,
                        onTableChange,
                        selectedRowKeys = [],
                        onSelectChange,
                    }) {

    const rowSelection = onSelectChange
        ? {
            selectedRowKeys,
            onChange: onSelectChange,
        }
        : undefined;

    return (
        <div className={cx('table-container')}>
            <AntTable
                bordered
                rowClassName={() => cx('fixed-row-height')}
                className={styles.customTable}
                columns={columns}
                dataSource={dataSources}
                loading={loading}
                rowKey="id"
                size="small"
                rowSelection={rowSelection}
                pagination={{
                    ...pagination,
                    showSizeChanger: true,
                    pageSizeOptions: ['5', '10', '20', '50'],
                    showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} bản ghi`,
                }}
                onChange={onTableChange}
                scroll={{ x: 'max-content' }}
            />
        </div>
    );
}

export default SmartTable;