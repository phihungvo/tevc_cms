import React from 'react';
import { Table as AntTable } from 'antd';
import classNames from 'classnames/bind';
import styles from './SmartTable.module.scss';

const cx = classNames.bind(styles);

function SmartTable({columns, dataSources, loading, pagination, onTableChange}) {

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
                size='small'
                pagination={{
                    ...pagination,
                    showSizeChanger: true,
                    pageSizeOptions: ['5', '10', '20'],
                    // position: ['topLeft']
                }}
                onChange={onTableChange}
                scroll={{ x: 'max-content'}}
            />
        </div>
    );
}

export default SmartTable;
